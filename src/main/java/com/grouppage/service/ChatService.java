package com.grouppage.service;

import com.grouppage.concurency.Process;
import com.grouppage.concurency.ProcessBuilder;
import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.domain.response.PostedMessage;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.auth.Principal;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ParticipantRepository participantRepository;
    private final ConversationRepository conversationRepository;
    private final PrivateMessageRepository privateMessageRepository;

    public ChatService(ParticipantRepository participantRepository,
                       ConversationRepository conversationRepository,
                       PrivateMessageRepository privateMessageRepository) {
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
        this.privateMessageRepository = privateMessageRepository;
    }


    public void handleNewChat(PostedMessage postedMessage, String receiver) throws WrongDataPostedException {
        if(!receiver.isEmpty() && null != postedMessage){
            throw new WrongDataPostedException("Posted data is invalid!");
        }
        Conversation conversation = new Conversation();
        Participant first = participantRepository.findById(postedMessage.getSenderId())
                .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!"));
        Participant second = participantRepository.findById(Long.parseLong(receiver))
                .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!"));
        conversation.setParticipants(Arrays.asList(first, second));
        conversation = conversationRepository.save(conversation);
        PrivateMessage privateMessage = new PrivateMessage();
        privateMessage.setConversation(conversation);
        privateMessage.setContent(postedMessage.getContent());
        privateMessage.setSender(first);
        privateMessageRepository.save(privateMessage);
    }

    public SocketMessage processNewPrivateMessage(SocketMessage socketMessage,
                                                  SimpMessagingTemplate simpMessagingTemplate) throws WrongDataPostedException {
        if (socketMessage.getType().equals(SocketMessage.Type.GROUP)){
            throw new WrongDataPostedException("Group message posted to private handler!");
        }
        Principal user = (Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Participant> participants = participantRepository.findAllByUserId(user.getId());
        List<Group> groups = participants.parallelStream()
                        .map(Participant::getGroup)
                        .collect(Collectors.toList());
        List<Conversation> conversations = conversationRepository.findAllByParticipantsIn(participants);
        List<Participant> fromConvs = conversations.parallelStream()
                .flatMap(p -> p.getParticipants().stream()).collect(Collectors.toList());
        CountDownLatch latch = new CountDownLatch(2);
        ConcurrencyService con = new ConcurrencyService(
                ProcessBuilder.create()
                        .withLatch(latch)
                        .withClass(List.class)
                        .withObject(participantRepository)
                        .withMethod("findAllByParticipantsIn")
                        .withParams(participants)
                        .build(),
                ProcessBuilder.create()
                        .withLatch(latch)
                        .withClass(List.class)
                        .withObject(participantRepository)
                        .withMethod("findAllByGroupIn")
                        .withParams(groups)
                        .build());


        return null;
    }
}
