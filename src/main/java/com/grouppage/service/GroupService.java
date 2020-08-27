package com.grouppage.service;

import com.grouppage.domain.entity.*;
import com.grouppage.domain.logicForAsync.GroupLogicForAsync;
import com.grouppage.domain.notmapped.*;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.repository.SignUpFormRepository;
import com.grouppage.domain.response.*;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.exception.PostNotFoundException;
import com.grouppage.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupService {

    private final GroupLogicForAsync groupLogicForAsync;

    private final AuthService authService;
    private final ChatService chatService;

    private final ExecService execService;

    private final String HASH = "#";

    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;
    private final SignUpFormRepository signUpFormRepository;

    @Autowired
    public GroupService(GroupLogicForAsync groupLogicForAsync,
                        AuthService authService,
                        ChatService chatService, ExecService execService, PostRepository postRepository,
                        ParticipantRepository participantRepository,
                        GroupRepository groupRepository, SignUpFormRepository signUpFormRepository) {
        this.groupLogicForAsync = groupLogicForAsync;
        this.authService = authService;
        this.chatService = chatService;
        this.execService = execService;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
        this.groupRepository = groupRepository;
        this.signUpFormRepository = signUpFormRepository;
    }

    public void handleNewPost(PostedPost postedPost) throws ExecutionException, InterruptedException {
//        Future<List<HashTag>> hashTags = this.getHashTagsFromPost(postedPost.getContent());
//        Post post = new Post();
//        post.setContent(postedPost.getContent());
//        post.setReactionCount(0);
//        CompletableFuture<Void> futureGroup = CompletableFuture.supplyAsync(
//                () -> this.groupRepository.findById(postedPost.getGroupId()).orElseThrow(
//                        () -> new GroupNotFoundException("Group id is wrong")
//                ),
//                execService.getExecutor()
//        ).thenAccept(post::setGroup);
//        CompletableFuture<Void> futureParticipant = CompletableFuture.supplyAsync(
//                () -> this.participantRepository.findById(postedPost.getParticipantId()).orElseThrow(
//                        () -> new ParticipantNotFountException("Given participant doesnt exists")
//                ),
//                execService.getExecutor()
//        ).thenAccept(post::setAuthor);
//        post.setHashTags(hashTags.get());
//        CompletableFuture.allOf(futureGroup, futureParticipant)
//                .thenAccept(
//                        (v) -> this.postRepository.save(post)
//                );
        this.chatService.processNewGroupPost(
                new SocketMessage(postedPost.getParticipantId(), postedPost.getContent(), Type.GROUP),
                postedPost.getGroupId());
    }

    public Page<PostResponse> getPostForGroupId(long groupId, Integer page, Integer size, String sort)throws GroupNotFoundException, AccessDeniedException {
        if(!this.checkIfUserIsParticipantInGroup(groupId))
            throw new AccessDeniedException("You do not participate in this group!");
        Pageable pageable = this.generatePageable(page, size, sort);
        List<PostResponse> posts =  postRepository.findAllByGroupIdFetchAuthor(groupId).stream()
                .map(p -> PostResponse.fromPost(p, groupId)).collect(Collectors.toList());
        return this.generatePage(posts, pageable);
    }



    public Page<GroupSearch> findGroupBySearchPhrase(String phrase, Integer size, String page, String sort, boolean member) throws NumberFormatException{
        Pageable pageable = this.generatePageable(Integer.parseInt(page), size, sort);
        List<GroupSearch> groups;
        if(false){
//            List<Participant> participants = this.participantRepository.findAllByUserFetchGroup(this.authService.getUserFromContext());
//            groups = participants.stream()
//                    .map(Participant::getGroup)
//                    .filter(Group.distinctByKeys(Group::getId))
//                    .filter(g -> g.getDescription().toUpperCase().contains(phrase.toUpperCase()) ||
//                            g.getName().toUpperCase().contains(phrase.toUpperCase()) ||
//                            g.getCategory().toUpperCase().contains(phrase.toUpperCase()))
//                    .map(GroupLight::fromGroup)
//                    .collect(Collectors.toList());
        }else {

            List<Group> groupy = this.groupRepository.proceedGroupSearch(phrase);
            List<Group> groupFromParticipants = this.participantRepository.findAllByUserFetchGroup(this.authService.getUserFromContext()).stream()
                    .map(Participant::getGroup).collect(Collectors.toList());
            groups = groupy.stream().map(g -> {
                if(groupFromParticipants.contains(g)){
                    return GroupSearch.fromGroup(g, true);
                }else{
                    return GroupSearch.fromGroup(g, false);
                }
            }).collect(Collectors.toList());
        }
        return this.generatePage(groups, pageable);
    }



    public Post upVote(long participantId, long postId) throws PostNotFoundException, AccessDeniedException, ParticipantNotFountException, ExecutionException, InterruptedException {
        Participant participant = this.participantRepository.findByIdFetchUser(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participan with id: "+ participantId + " doesnt exists!")
        );
        if(this.checkOwnerOfParticipant(participant))
            throw new AccessDeniedException("You dont own this participant");
        Future<Post> future = this.groupLogicForAsync.upVote(participant, postId);
        return future.get();

    }

    public Post downVote(long participantId, long postId) throws PostNotFoundException, AccessDeniedException, ParticipantNotFountException, ExecutionException, InterruptedException {
        Participant participant = this.participantRepository.findByIdFetchUser(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participant with id: " + participantId + " doesnt exists!")
        );
        if(this.checkOwnerOfParticipant(participant))
            throw new AccessDeniedException("You dont own this participant");
        Future<Post> future = this.groupLogicForAsync.removeVote(participant, postId);
        return future.get();
    }

    private <T> Page<T> generatePage(List<T> list, Pageable pageable){
        int start = Math.toIntExact(pageable.getOffset());
        long end = Math.min((start + pageable.getPageSize()), list.size());
        return new PageImpl<>(list.subList(start, Math.toIntExact(end)), pageable, list.size());
    }

     private boolean checkOwnerOfParticipant(Participant participant) {
        User user = this.authService.getUserFromContext();
        return participant.getUser().getId() != user.getId();
    }
    private boolean checkIfUserIsParticipantInGroup(long groupId){
        User user = this.authService.getUserFromContext();
        List<Participant> participants = this.participantRepository.findAllByUserFetchGroup(user);
        return participants.stream().anyMatch(p -> p.getGroup().getId() == groupId);
    }

    private Pageable generateDefaultPageable(){
        return this.generatePageable(1, 20, "nosort");
    }
    private Pageable generateDefaultPageablePageNumber(int page) {
        return this.generatePageable(page, 20, "nothing");
    }
    private Pageable generatePageable(Integer page, Integer size, String sort) {
        if(page == null)
            page = 1;
        if(size == null)
            size = 20;
        if(Objects.isNull(sort)){
            return PageRequest.of(page - 1, size);
        }else{
            // TODO IMPL OF SORTING
            return PageRequest.of(page - 1, size);
        }

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

    public List<DashboardResponse> generateDashboard() throws InterruptedException, ExecutionException, TimeoutException {
        User user = this.authService.getUserFromContext();
        Future<List<DashboardResponse>> future =  this.groupLogicForAsync.generateDashboard(user);
        return future.get();
    }

    public Future<Group> saveNewGroup(RequestNewGroup requestNewGroup) {
        return this.groupLogicForAsync.handleNewGroup(requestNewGroup,
                this.authService.getUserFromContext());
    }

    public GroupForm getGroupFromInviteCode(String id) {
        Group group = this.groupRepository.findByInviteCode(id).orElseThrow(
                () -> new GroupNotFoundException("Invitation is invalid!")
        );
        GroupForm form = null;
        if(group.isForm()){
            Optional<SignUpForm> optional = this.signUpFormRepository.findByGroupId(group.getId());
            if(optional.isPresent()){
                form = optional.get().getForm();
                form.getForm().put("nickname", "");
            }
        }else{
            form = new GroupForm(new HashMap<String, String>(){{put("nickname", "");}});
        }
        return form;
    }

    public void handleNewParticipant(Map<String, String> groupForm, String id) {
        User user = this.authService.getUserFromContext();
        this.groupLogicForAsync.
                handleNewParticipant(groupForm, id,
                        user).handleAsync(
                (p, t) -> {
                    SocketMessage message = new SocketMessage();
                    message.setParticipantId(0);
                    String content;
                    if(t != null){
                        message.setType(Type.ERROR);
                        content = "Error occured -> ".concat(t.getMessage());
                    }else{
                        message.setType(Type.NOTIFICATION);
                        content = "Success";
                    }
                    message.setContent(content);
                    this.chatService.sendMessageOrPost(Collections.singletonList(user.getId()),
                            new SocketOutMessage(message.getParticipantId(), p.getGroup().getId(),
                                    message.getContent(), message.getType()));
                    return null;
                }
        );

    }

    public List<ParticipantLight> getAllParticipants(long groupId) {
        return this.participantRepository.findAllByGroupId(groupId).stream()
                .map(ParticipantLight::fromParticipant).collect(Collectors.toList());
    }
}
