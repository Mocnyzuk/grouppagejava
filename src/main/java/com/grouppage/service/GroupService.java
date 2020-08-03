package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.logicForAsync.GroupLogicForAsync;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.DashboardResponse;
import com.grouppage.domain.response.InviteParticipant;
import com.grouppage.domain.response.PostedPost;
import com.grouppage.domain.response.RequestNewGroup;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.exception.PostNotFoundException;
import com.grouppage.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

@Service
@Transactional
public class GroupService {

    private final GroupLogicForAsync groupLogicForAsync;

    private final AuthService authService;

    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;
    private final GroupRepository groupRepository;

    @Autowired
    public GroupService(GroupLogicForAsync groupLogicForAsync,
                        AuthService authService,
                        PostRepository postRepository,
                        ParticipantRepository participantRepository,
                        GroupRepository groupRepository) {
        this.groupLogicForAsync = groupLogicForAsync;
        this.authService = authService;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
        this.groupRepository = groupRepository;
    }

    public void handleNewPost(PostedPost postedPost, Group group){

    }

    public Page<Post> getPostForGroupId(long groupId, Integer page, Integer size, String sort)throws GroupNotFoundException, AccessDeniedException {
        if(!this.checkIfUserIsParticipantInGroup(groupId))
            throw new AccessDeniedException("You do not participate in this group!");
        Pageable pageable = this.generateDefaultPageable();
        return postRepository.findAllByGroupId(groupId, pageable);
    }



    public Page<Group> findGroupBySearchPhrase(String phrase, String sort) {
        Pageable pageable = this.generateDefaultPageable();
        return this.groupRepository.proceedGroupSearch(phrase, pageable);
    }

    public Post upVote(long participantId, long postId) throws PostNotFoundException, AccessDeniedException, ParticipantNotFountException, ExecutionException, InterruptedException {
        Participant participant = this.participantRepository.findById(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participan with id: "+ participantId + " doesnt exists!")
        );
        if(this.checkOwnerOfParcitipant(participant))
            throw new AccessDeniedException("You dont own this participant");
        Future<Post> future = this.groupLogicForAsync.upVote(participant, postId);
        return future.get();

    }

    public Post downVote(long participantId, long postId) throws PostNotFoundException, AccessDeniedException, ParticipantNotFountException, ExecutionException, InterruptedException {
        Participant participant = this.participantRepository.findById(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participant with id: " + participantId + " doesnt exists!")
        );
        if(this.checkOwnerOfParcitipant(participant))
            throw new AccessDeniedException("You dont own this participant");
        Future<Post> future = this.groupLogicForAsync.removeVote(participant, postId);
        return future.get();
    }

     private boolean checkOwnerOfParcitipant(Participant participant) {
        User user = this.authService.getUserFromContext();
        return participant.getUser().getId() != user.getId();
    }
    private boolean checkIfUserIsParticipantInGroup(long groupId){
        User user = this.authService.getUserFromContext();
        List<Participant> participants = this.participantRepository.findAllByUserFetchGroup(user);
        return participants.stream().anyMatch(p -> p.getGroup().getId() == groupId);
    }
    private Pageable generateDefaultPageable(){
        return this.generatePageable(0, 20, "nothing");
    }
    private Pageable generatePageable(Integer page, Integer size, String sort) {
        if(page == null)
            page = 0;
        if(size == null)
            size = 20;
        if(Objects.isNull(sort)){
            return PageRequest.of(page, size);
        }else{
            // TODO IMPL OF SORTING
            return PageRequest.of(page, size);
        }

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

    public GroupLight getGroupFromInviteCode(String id) {
        Group group = this.groupRepository.findByInviteCode(id).orElseThrow(
                () -> new GroupNotFoundException("Invitation is invalid!")
        );
        return GroupLight.fromGroup(group);
    }

    public Future<Participant> handleNewParticipant(InviteParticipant inviteParticipant, String id) {
        return this.groupLogicForAsync.
                handleNewParticipant(inviteParticipant, id,
                        this.authService.getUserFromContext());

    }
}
