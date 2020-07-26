package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.domain.response.PostedMessage;
import com.grouppage.exception.ConversationNotFoundException;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.auth.Principal;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import java.net.Socket;
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
    private final GroupRepository groupRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final ExecService execService;

    private final String HASH = "H";
    private final char HASH_CHAR = '#';


    public ChatService(ParticipantRepository participantRepository,
                       ConversationRepository conversationRepository,
                       PrivateMessageRepository privateMessageRepository,
                       GroupRepository groupRepository,
                       SimpMessagingTemplate simpMessagingTemplate,
                       ExecService execService) {
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
        this.privateMessageRepository = privateMessageRepository;
        this.groupRepository = groupRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
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

    public void processNewPrivateMessage(SocketMessage socketMessage,
                                        long conversationId) throws WrongDataPostedException, ExecutionException, InterruptedException {
        if (socketMessage.getType().equals(SocketMessage.Type.GROUP)){
            throw new WrongDataPostedException("Group message posted to private handler!");
        }
        Future<Conversation> conversationFuture = execService.executeCallable(() -> this.conversationRepository.findById(conversationId).orElseThrow(
                () -> new ConversationNotFoundException("Conversation doesnt exists with id: "+ conversationId)
        ));
        List<Participant> fromConv = conversationFuture.get().getParticipants();
        Future<PrivateMessage> messageFuture = execService.executeCallable( () -> {
            PrivateMessage message = new PrivateMessage();
            if(socketMessage.getContent() == null){
                socketMessage.setContent("");
            }
            message.setContent(socketMessage.getContent());
            message.setSender(fromConv.stream().filter(p -> p.getId() == socketMessage.getParticipantId()).findFirst().orElseThrow(
                    () -> new ParticipantNotFountException("Participant doesnt exists with id: "+ socketMessage.getParticipantId())
            ));
            message.setConversation(conversationFuture.get());
            message.setType(socketMessage.getType().name());
            return message;
                }
        );

        List<Long> userIds = fromConv.stream().map(p -> p.getUser().getId()).distinct().collect(Collectors.toList());
        this.sendMessageOrPost(userIds, messageFuture.get());

    }
    public void processNewGroupPost(SocketMessage socketMessage,
                                    long groupId) throws ExecutionException, InterruptedException {
        Future<List<HashTag>> hashTags = this.getHashTagsFromPost(socketMessage.getContent());
        Future<Group> groupFuture = execService.executeCallable(
                () -> this.groupRepository.findById(groupId).orElseThrow(
                () -> new GroupNotFoundException("Group doesnt exists with id: "+ groupId)
        ));
        Future<List<Participant>> participantFuture = execService.executeCallable(
                () -> this.participantRepository.findAllByGroupId(groupId)
        );
        Future<Post> messageFuture = execService.executeCallable( () -> {
                    Post message = new Post();
                    message.setContent(socketMessage.getContent());
                    message.setAuthor(participantFuture.get().stream().filter(p -> p.getId() == socketMessage.getParticipantId()).findFirst().orElseThrow(
                            () -> new ParticipantNotFountException("Participant doesnt exists with id: "+ socketMessage.getParticipantId())
                    ));
                    message.setGroup(groupFuture.get());
                    message.setReactionCount(0);
                    message.setHashTags(hashTags.get());

                    return message;
                }
        );
        List<Long> userIds = participantFuture.get().stream().map(p -> p.getUser().getId()).distinct().collect(Collectors.toList());
        this.sendMessageOrPost(userIds, messageFuture.get());
    }

    private Future<List<HashTag>> getHashTagsFromPost(String post){
        final String[] content = {post};
        return execService.executeCallable(() -> {
            List<HashTag> hashtags = new ArrayList<>();
            while(content[0].contains(HASH)){
                int hashIndex = content[0].indexOf(HASH);
                content[0] = content[0].substring(hashIndex);
                int index = 0;

                while((content[0].charAt(index) != ' ') && index < 4){
                    index++;
                }
                if(index == 4){

                    hashtags.add(new HashTag(content[0].substring(0, content[0].indexOf(' '))));
                }else{
                    content[0] = content[0].substring(index);
                }
            }
            return hashtags;
        });
    }
    private void sendMessageOrPost(List<Long> userIds, Object message){
        for (Long userId : userIds) {
            execService.executeCallable(
                    () -> {
                        this.simpMessagingTemplate.convertAndSend("/topic/" + userId, message);
                        return null;
                    }
            );
        }
    }


    /**
     * test COde for managing messages
     * @param socketMessage
     * @param simpMessagingTemplate
     */
    private void testCode(SocketMessage socketMessage, SimpMessagingTemplate simpMessagingTemplate){
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
    }


}
