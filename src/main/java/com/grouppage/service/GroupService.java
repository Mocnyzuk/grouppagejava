package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.GroupSearch;
import com.grouppage.domain.response.PostedPost;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.PostNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class GroupService {

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public GroupService(GroupRepository groupRepository, PostRepository postRepository, ParticipantRepository participantRepository) {
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
    }

    public void handleNewPost(PostedPost postedPost, Group group){

    }

    public List<Post> getPostForGroupId(Group group)throws GroupNotFoundException {
        long groupId = group.getId();
        return postRepository.findAllByGroup(
                groupRepository.findById(groupId).orElseThrow(
                        () -> new GroupNotFoundException("Group with id: "+groupId+" doesnt exists!")
                )
        );
    }

    public List<GroupSearch> findGroupBySearchPhrase(String phrase) {
        return null;
    }

    public synchronized Post upVote(Participant participant, long postId) throws PostNotFoundException{
        Optional<Post> postBase = this.postRepository.findById(postId);
        if(postBase.isPresent()){
            Post postBaseGet = postBase.get();
            postBaseGet.setReactionCount(postBaseGet.getReactionCount() + 1);

            Post result = this.postRepository.save(postBaseGet);
            List<Post> liked = participant.getLikedPosts();
            liked.add(postBaseGet);
            this.participantRepository.save(participant);
            return result;
        }
        throw new PostNotFoundException("Post with id: "+postId+ " doesnt exists!");

    }

    public synchronized Post downVote(Participant participant, long postId) {
        Optional<Post> postBase = this.postRepository.findById(postId);
        if(postBase.isPresent() && participant.getLikedPosts().contains(postBase.get())){
            Post post = postBase.get();
            post.setReactionCount(post.getReactionCount() - 1);
            List<Post> liked = participant.getLikedPosts();
            liked.remove(post);
            participant.setLikedPosts(liked);
            this.participantRepository.save(participant);
            return this.postRepository.save(post);
        }
        throw new PostNotFoundException("Post with id: "+postId+" doesnt exists!");
    }
}
