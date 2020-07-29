package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.logicForAsync.GroupLogicForAsync;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.GroupSearch;
import com.grouppage.domain.response.PostedPost;
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
import java.util.Optional;

@Service
@Transactional
public class GroupService {

    private final GroupLogicForAsync groupLogicForAsync;

    private final AuthService authService;

    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public GroupService(GroupLogicForAsync groupLogicForAsync, AuthService authService, PostRepository postRepository, ParticipantRepository participantRepository) {
        this.groupLogicForAsync = groupLogicForAsync;
        this.authService = authService;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
    }

    public void handleNewPost(PostedPost postedPost, Group group){

    }

    public Page<List<Post>> getPostForGroupId(long groupId, Integer page, Integer size, String sort)throws GroupNotFoundException, AccessDeniedException {
        if(!this.checkIfUserIsParticipantInGroup(groupId))
            throw new AccessDeniedException("You do not participate in this group!");
        Pageable pageable = this.generatePageable(page, size, sort);
        return postRepository.findAllByGroupId(groupId, pageable);
    }



    public List<GroupSearch> findGroupBySearchPhrase(String phrase) {
        return null;
    }

    public int upVote(long participantId, long postId) throws PostNotFoundException, AccessDeniedException, ParticipantNotFountException{
        Participant participant = this.participantRepository.findById(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participan with id: "+ participantId + " doesnt exists!")
        );
        if(this.checkOwnerOfParcitipant(participant))
            throw new AccessDeniedException("You dont own this participant");
        this.groupLogicForAsync.upVote(participant, postId);
        return this.postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Post with id: "+postId+" doesnt exists")
        ).getReactionCount()+1;

    }

    public int downVote(long participantId, long postId) throws PostNotFoundException, AccessDeniedException, ParticipantNotFountException{
        Participant participant = this.participantRepository.findById(participantId).orElseThrow(
                () -> new ParticipantNotFountException("Participant with id: " + participantId + " doesnt exists!")
        );
        if(this.checkOwnerOfParcitipant(participant))
            throw new AccessDeniedException("You dont own this participant");
        this.groupLogicForAsync.removeVote(participant, postId);
        return this.postRepository.findById(postId).orElseThrow(
                () -> new PostNotFoundException("Post with id: "+postId+" doesnt exists")
        ).getReactionCount()-1;

    }

     private boolean checkOwnerOfParcitipant(Participant participant) {
        User user = this.authService.getUserFromContext();
        return participant.getUser().getId() != user.getId();
    }
    private boolean checkIfUserIsParticipantInGroup(long groupId){
        User user = this.authService.getUserFromContext();
        List<Participant> participants = this.participantRepository.findAllByUser(user);
        return participants.stream().anyMatch(p -> p.getGroup().getId() == groupId);
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
}
