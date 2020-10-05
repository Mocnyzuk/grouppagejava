package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.*;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.domain.response.AddParticipantRequest;
import com.grouppage.domain.response.EditConversation;
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
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

    private static final String HASH = "H";

    public ChatService(ParticipantRepository participantRepository,
                       ConversationRepository conversationRepository,
                       PrivateMessageRepository privateMessageRepository,
                       GroupRepository groupRepository,
                       PostRepository postRepository,
                       SimpMessagingTemplate simpMessagingTemplate,
                       AuthService authService,
                       ExecService execService) {
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
        this.privateMessageRepository = privateMessageRepository;
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.simpMessagingTemplate = simpMessagingTemplate;
        this.authService = authService;
        this.execService = execService;
    }


    public void handleNewChat(SocketMessage socketMessage, String receiver)
            throws WrongDataPostedException, ExecutionException, InterruptedException, AccessDeniedException {
        if(!receiver.isEmpty() && null == socketMessage){
            throw new WrongDataPostedException("Posted data is invalid!");
        }
        Conversation conversation = new Conversation();
        Future<Participant> first = execService.executeCallable(() -> participantRepository.findByIdFetchUserFetchGroup(socketMessage.getParticipantId())
            .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!")));
        Future<Participant> second = execService.executeCallable(() -> participantRepository.findByIdFetchUser(Long.parseLong(receiver))
            .orElseThrow(() -> new WrongDataPostedException("Posted data is invalid!")));
        List<Participant> partis = Arrays.asList(first.get(), second.get());
        conversation.setParticipants(partis);
        Conversation conv = conversationRepository.save(conversation);
        if(socketMessage.getType() == Type.NEW){
            Participant pierwszy = first.get();
            Participant drugi = second.get();
            List<Long> userIds = Arrays.asList(drugi.getUser().getId(), pierwszy.getUser().getId());
            List<Participant> all = Arrays.asList(pierwszy, drugi);
            List<ParticipantLight> pLights = Stream.of(pierwszy, drugi).map(ParticipantLight::fromParticipant).collect(Collectors.toList());
            System.out.println(Arrays.toString(userIds.toArray()));
            all.forEach(part -> this.sendMessageOrPost(part.getUser().getId(),
                    new ConversationMessage(part.getUser().getId() == (pierwszy.getUser().getId()) ? pierwszy.getId() : drugi.getId(),
                            pierwszy.getGroup().getId(),
                            conv.getId(), pLights, socketMessage.getType())));

        }else{
            this.processNewPrivateMessage(socketMessage, conv.getId());
        }
    }

    public void processNewPrivateMessage(SocketMessage socketMessage,
                                        long conversationId) throws WrongDataPostedException, AccessDeniedException {
        if (!socketMessage.getType().equals(Type.CHAT)){
            throw new WrongDataPostedException("Group message posted to private handler!");
        }
        Conversation conversationFuture = this.conversationRepository.findByIdFetchParticipants(conversationId).orElseThrow(
                () -> new ConversationNotFoundException("COnv not foud with id: "+conversationId)
        );
        List<Participant> fromConv = conversationFuture.getParticipants();
        if(fromConv.stream().noneMatch(p -> p.getId() == socketMessage.getParticipantId()))
            throw new AccessDeniedException("You have no permission do send messages here!");
        Future<PrivateMessage> messageFuture = execService.executeCallable(() -> {
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
        this.sendMessageOrPost(userIds, new SocketOutMessage(socketMessage.getParticipantId(), conversationId, socketMessage.getContent(), socketMessage.getType()));
        execService.executeCallable(() -> privateMessageRepository.save(messageFuture.get()));

    }
    public void processNewGroupPost(SocketMessage socketMessage,
                                    long groupId) throws ExecutionException, InterruptedException, WrongDataPostedException, AccessDeniedException {
        if(socketMessage.getType() != Type.GROUP){
            throw new WrongDataPostedException("Message is not a post for group!");
        }
//        if(this.checkOwnerOfParcitipant(socketMessage.getParticipantId()))
//            throw new AccessDeniedException("You dont own this participant");
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
        postRepository.save(messageFuture.get());
        if(participantFuture.get().stream().noneMatch(p -> p.getId() == socketMessage.getParticipantId()))
            throw new AccessDeniedException("You have no permission do send posts here!");
        List<Long> userIds = participantFuture.get().stream().map(p -> p.getUser().getId()).distinct().collect(Collectors.toList());

        this.sendMessageOrPost(userIds,
                new SocketOutMessage(socketMessage.getParticipantId(), groupId, socketMessage.getContent(), socketMessage.getType()));
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
    public void sendMessageOrPost(List<Long> userIds, SocketOutMessage message){
        for (Long userId : userIds) {
            execService.executeRunnable(
                    () -> this.simpMessagingTemplate.convertAndSend("/topic/" + userId, message)
            );
        }
    }
    private void sendMessageOrPost(List<Long> userIds, NewParticipantMessage newParticipantMessage) {
        for (Long userId : userIds) {
            execService.executeRunnable(
                    () -> this.simpMessagingTemplate.convertAndSend("/topic/" + userId, newParticipantMessage)
            );
        }
    }
    public void sendMessageOrPost(long userId, ConversationMessage message){
            execService.executeRunnable(
                    () -> this.simpMessagingTemplate.convertAndSend("/topic/" + userId, message)
            );
    }
    private boolean checkOwnerOfParcitipant(long participantId) {
        Participant participant = this.participantRepository.findByIdFetchUser(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participant with id: " + participantId + " doesnt exists!")
        );
        User user = this.authService.getUserFromContext();
        return participant.getUser().getId() != user.getId();
    }
    public void addNewParticipantToConversation(AddParticipantRequest request) throws ExecutionException, InterruptedException {
        Conversation conv = this.conversationRepository.findByIdFetchParticipants(request.getConversationId())
                .orElseThrow(() -> new ConversationNotFoundException("Conversation with id: "+request.getConversationId()+ " doesnt exists!"));
        Future<Participant> futurePart = execService.executeCallable(()->participantRepository.findById(request.getParticipantId())
                .orElseThrow(() -> new ParticipantNotFountException("Participant with id: "+ request.getParticipantId()+" doesnt exists!")));
        List<Participant> fromConv = conv.getParticipants();
        Participant nowy = futurePart.get();
        fromConv.add(nowy);
        conv.setParticipants(fromConv);
        conversationRepository.save(conv);
        this.sendMessageOrPost(conv.getParticipants().stream().map(p -> p.getUser().getId()).collect(Collectors.toList()),
                new NewParticipantMessage(nowy.getId(), conv.getId(),
                        nowy.getNickname(), Type.CONVERSATION));
    }




    public List<ConversationLight> getConversations() {
        User user = this.authService.getUserFromContext();
        List<Conversation> conversations = this.conversationRepository.findAllByFetchParticipants();
        return conversations.stream()
                .filter(c -> c.getParticipants().stream().anyMatch(p -> p.getUser().getId() == user.getId()))
                .map(ConversationLight::fromConversation)
                .filter(Group.distinctByKeys(ConversationLight::getId))
                .collect(Collectors.toList());
    }

    public ConversationInfoWithMessages getAllMessages(long conversationId) {
        Conversation conversation = this.conversationRepository.findByIdFetchParticipants(conversationId).orElseThrow(
                () -> new ConversationNotFoundException("conversation not found")
        );
        User user = this.authService.getUserFromContext();
        Participant myParticipantId = conversation.getParticipants().stream().filter(p -> p.getUser().getId() == user.getId()).findFirst().orElseThrow(
                () -> new AccessDeniedException(" you does not take part in this conv")
        );
        return new ConversationInfoWithMessages(myParticipantId.getId(),
                myParticipantId.getGroup().getId(),
                conversation.getId(),
                conversation.getParticipants().stream().map(ParticipantLight::fromParticipant).collect(Collectors.toList()),
                this.privateMessageRepository.findAllByConversationId(conversationId).stream()
                .map(SocketMessage::fromPrivateMessage).collect(Collectors.toList()));
    }

    public void editConversation(EditConversation edit) {
        //TODO edit conv
    }

    public void removeMeFromConversation(long conversationId) {
        User user = this.authService.getUserFromContext();
        this.conversationRepository.findByIdFetchParticipants(conversationId).ifPresent(c -> {
            List<Participant> participants = c.getParticipants().stream()
                    .filter(p -> !p.getUser().equals(user)).collect(Collectors.toList());
            c.setParticipants(participants);
            this.conversationRepository.save(c);
        });
    }
}
