package com.grouppage.domain.logicForAsync;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.notmapped.PostLight;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.response.DashboardResponse;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.exception.PostNotFoundException;
import com.grouppage.service.ExecService;
import com.grouppage.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.security.cert.CollectionCertStoreParameters;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupLogicForAsync {

    private final ExecService execService;

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;

    @Autowired
    public GroupLogicForAsync(ExecService execService, GroupRepository groupRepository, PostRepository postRepository, ParticipantRepository participantRepository) {
        this.execService = execService;
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
    }

    public Future<Post> upVote (Participant participant, long postId) throws PostNotFoundException, ParticipantNotFountException{
        return this.execService.executeCallable(() -> {
            Optional<Post> postBase = this.postRepository.findById(postId);
            if (postBase.isPresent()) {
                Post postBaseGet = postBase.get();
                postBaseGet.setReactionCount(postBaseGet.getReactionCount() + 1);

                Post result = this.postRepository.save(postBaseGet);
                List<Post> liked = participant.getLikedPosts();
                liked.add(result);
                participant.setLikedPosts(liked);
                this.participantRepository.save(participant);
                return result;
            }
            throw new PostNotFoundException("Post with id: " + postId + " doesnt exists!");
        });
    }
    public Future<Post> removeVote(Participant participant, long postId)throws PostNotFoundException, ParticipantNotFountException{
        return this.execService.executeCallable(() -> {
            Optional<Post> postBase = this.postRepository.findById(postId);
            if(postBase.isPresent() && participant.getLikedPosts().contains(postBase.get())) {
                Post post = postBase.get();
                post.setReactionCount(post.getReactionCount() - 1);
                List<Post> liked = participant.getLikedPosts();
                liked.remove(post);
                participant.setLikedPosts(liked);
                this.participantRepository.save(participant);
                return this.postRepository.save(post);
            }
            throw new PostNotFoundException("Post with id: "+postId+" doesnt exists!");
        });
    }

    public Future<List<DashboardResponse>> generateDashboard(User user) {
        return this.execService.executeCallable(() -> {
            List<DashboardResponse> list = new ArrayList<>(5);
            List<Participant> participants = this.participantRepository.findAllByUserIdFetchGroup(user.getId());
            List<Group> groups = participants
                    .stream()
                    .map(Participant::getGroup)
                    .filter(Group.distinctByKeys(Group::getId))
                    .collect(Collectors.toList());
            List<Post> posts = this.postRepository.findLatestPostFromGroups(groups);
            groups.forEach(g -> {
                DashboardResponse response = new DashboardResponse();
                response.setPosts(
                        posts.stream().filter(p -> p.getGroup().getId() == g.getId())
                                .sorted(Comparator.comparing(Post::getCreatedDate).reversed())
                                .limit(5)
                                .map(PostLight::fromPost)
                                .collect(Collectors.toList())
                );
                response.setGroup(GroupLight.fromGroup(g));
                list.add(response);
            });
            return list;
        });
    }
}
