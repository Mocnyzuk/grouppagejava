package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.domain.response.AddParticipantRequest;
import com.grouppage.exception.ConversationNotFoundException;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.auth.AuthService;
import com.grouppage.service.auth.Principal;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;

@Service
@Transactional
public class ChatService {

    private final ParticipantRepository participantRepository;
    private final ConversationRepository conversationRepository;
    private final PrivateMessageRepository privateMessageRepository;
    private final GroupRepository groupRepository;
    private final PostRepository postRepository;

    private final SimpMessagingTemplate simpMessagingTemplate;

    private final AuthService authService;
    private final ExecService execService;

    private final String HASH = "H";
    private final char HASH_CHAR = '#';


    public ChatService(ParticipantRepository participantRepository,
                       ConversationRepository conversationRepository,
                       PrivateMessageRepository privateMessageRepository,
                       GroupRepository groupRepository,
                       PostRepository postRepository, SimpMessagingTemplate simpMessagingTemplate,
                       AuthService authService, ExecService execService) {
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
        this.privateMessageRepository = privateMessageRepository;
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authService = authService;
        this.execService = execService;
    }


    public void handleNewChat(SocketMessage socketMessage, String receiver) throws WrongDataPostedException, ExecutionException, InterruptedException {
        if(!receiver.isEmpty() && null == socketMessage){
            throw new WrongDataPostedException("Posted data is invalid!");
        }
        Conversation conversation = new Conversation();
        Future<Participant> first = execService.executeCallable(() -> participantRepository.findById(socketMessage.getParticipantId())
            .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!")));
        Future<Participant> second = execService.executeCallable(() -> participantRepository.findById(Long.parseLong(receiver))
            .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!")));
        List<Participant> partis = Arrays.asList(first.get(), second.get());

        conversation.setParticipants(partis);
        Conversation conv = conversationRepository.save(conversation);
        System.out.println("CONVERSATION ID: " + conv.getId() );
        this.processNewPrivateMessage(socketMessage, conv.getId());
    }

    public void processNewPrivateMessage(SocketMessage socketMessage,
                                        long conversationId) throws WrongDataPostedException, ExecutionException, InterruptedException, AccessDeniedException {
        if (!socketMessage.getType().equals(SocketMessage.Type.CHAT)){
            throw new WrongDataPostedException("Group message posted to private handler!");
        }
        if(this.checkOwnerOfParcitipant(socketMessage.getParticipantId()))
            throw new AccessDeniedException("You dont own this participant");
        Conversation conversationFuture = this.conversationRepository.findById(conversationId).orElseThrow(
                () -> new ConversationNotFoundException("COnv not foud with id: "+conversationId)
        );
        List<Participant> fromConv = conversationFuture.getParticipants();
        if(fromConv.stream().noneMatch(p -> p.getId() == socketMessage.getParticipantId()))
            throw new AccessDeniedException("You have no permission do send messages here!");
        Future<PrivateMessage> messageFuture = execService.executeCallable( () -> {
            PrivateMessage message = new PrivateMessage();
            if(socketMessage.getContent() == null){
                socketMessage.setContent("");
            }
            message.setContent(socketMessage.getContent());
            message.setSender(fromConv.stream().filter(p -> p.getId() == socketMessage.getParticipantId()).findFirst().orElseThrow(
                    () -> new ParticipantNotFountException("Participant doesnt exists with id: "+ socketMessage.getParticipantId())
            ));
            message.setConversation(conversationFuture);
            message.setType(socketMessage.getType().name());
            return message;
                }
        );

        List<Long> userIds = fromConv.stream().map(p -> p.getUser().getId()).distinct().collect(Collectors.toList());
        this.sendMessageOrPost(userIds, socketMessage);
        execService.executeCallable(() -> privateMessageRepository.save(messageFuture.get()));

    }
    public void processNewGroupPost(SocketMessage socketMessage,
                                    long groupId) throws ExecutionException, InterruptedException, WrongDataPostedException, AccessDeniedException {
        if(socketMessage.getType() != SocketMessage.Type.GROUP){
            throw new WrongDataPostedException("Message is not a post for group!");
        }
        if(this.checkOwnerOfParcitipant(socketMessage.getParticipantId()))
            throw new AccessDeniedException("You dont own this participant");
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
        if(participantFuture.get().stream().noneMatch(p -> p.getId() == socketMessage.getParticipantId()))
            throw new AccessDeniedException("You have no permission do send posts here!");
        List<Long> userIds = participantFuture.get().stream().map(p -> p.getUser().getId()).distinct().collect(Collectors.toList());

        this.sendMessageOrPost(userIds, socketMessage);
        Post post = messageFuture.get();
        execService.executeCallable(() -> postRepository.save(post));
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
    private void sendMessageOrPost(List<Long> userIds, SocketMessage message){
        for (Long userId : userIds) {
            execService.executeRunnable(
                    () -> this.simpMessagingTemplate.convertAndSend("/topic/" + userId, message)
            );
        }
    }
    private boolean checkOwnerOfParcitipant(long participantId) {
        Participant participant = this.participantRepository.findById(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participant with id: " + participantId + " doesnt exists!")
        );
        User user = this.authService.getUserFromContext();
        return participant.getUser().getId() != user.getId();
    }
    public void addNewParticipantToConversation(AddParticipantRequest request) throws ExecutionException, InterruptedException {
        Future<Conversation> futureConv = execService.executeCallable(()->conversationRepository.findById(request.getConversationId())
                .orElseThrow(() -> new ConversationNotFoundException("Conversation with id: "+request.getConversationId()+ " doesnt exists!")));
        Future<Participant> futurePart = execService.executeCallable(()->participantRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new ParticipantNotFountException("Participant with id: "+ request.getParticipantId()+" doesnt exists!")));
        Conversation conv = futureConv.get();
        List<Participant> fromConv = conv.getParticipants();
        fromConv.add(futurePart.get());
        conv.setParticipants(fromConv);
        conversationRepository.save(conv);
    }





    /**
     * test COde for managing messages
     * @param socketMessage
     * @param simpMessagingTemplate
     */
    private void testCode(SocketMessage socketMessage, SimpMessagingTemplate simpMessagingTemplate){
        Principal user = (Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Participant> participants = participantRepository.findAllByUserId(user.getId());
        //new SimpleAsyncTaskExecutor();
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
                    () -> simpMessagingTemplate.convertAndSend(String.format("%s%s","/topic/", String.valueOf(aLong)), socketMessage)
            );
        }
    }



}
