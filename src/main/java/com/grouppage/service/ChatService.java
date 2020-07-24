package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
public class ChatService {

    private final ParticipantRepository participantRepository;
    private final ConversationRepository conversationRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final ExecService execService;


    public ChatService(ParticipantRepository participantRepository,
                       ConversationRepository conversationRepository,
                       PrivateMessageRepository privateMessageRepository,
                       ExecService execService) {
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
        this.privateMessageRepository = privateMessageRepository;
        this.execService = execService;
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

        Future<List<Group>> futureGroups = execService.executeCallable( () -> participants.stream()
                .map(Participant::getGroup)
                .collect(Collectors.toList()));

        Future<List<Conversation>> futureConversations = execService.executeCallable(
                () -> conversationRepository.findAllByParticipantsIn(participants)
        );



        Future<List<Participant>> futurePartiFromGroups = execService.executeCallable(
                () ->   participantRepository.findAllByGroupIn(futureGroups.get())
        );
        Future<List<Participant>> futurePartiFromParti = execService.executeCallable(
                () -> futureConversations.get().stream().flatMap(p -> p.getParticipants().stream()).collect(Collectors.toList())
        );

        List<Long> userId = new ArrayList<>();
        try {
            List<Participant> participants1 = futurePartiFromGroups.get();
            participants1.addAll(futurePartiFromParti.get());
            userId = participants1.stream()
                    .map(p -> p.getUser().getId()).distinct().collect(Collectors.toList());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        for (Long aLong : userId) {
            execService.executeRunnable(
                    () -> simpMessagingTemplate.convertAndSend("/topic/" + aLong, socketMessage)
            );
        }

        return null;
    }
}
