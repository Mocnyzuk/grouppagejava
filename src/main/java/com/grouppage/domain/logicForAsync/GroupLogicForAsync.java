package com.grouppage.domain.logicForAsync;

import com.grouppage.domain.entity.*;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.notmapped.PostLight;
import com.grouppage.domain.repository.*;
import com.grouppage.domain.response.DashboardResponse;
import com.grouppage.domain.response.InviteParticipant;
import com.grouppage.domain.response.RequestNewGroup;
import com.grouppage.exception.GroupNotFoundException;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.exception.PostNotFoundException;
import com.grouppage.exception.ReactionNotFoundException;
import com.grouppage.service.ExecService;
import com.sun.xml.internal.ws.api.model.wsdl.WSDLOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Service
@Transactional
public class GroupLogicForAsync {

    private final ExecService execService;

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final ParticipantRepository participantRepository;
    private final ReactionRepository reactionRepository;
    private final SignUpFormRepository signUpFormRepository;

    @Autowired
    public GroupLogicForAsync(ExecService execService, GroupRepository groupRepository, PostRepository postRepository, ParticipantRepository participantRepository, ReactionRepository reactionRepository, SignUpFormRepository signUpFormRepository) {
        this.execService = execService;
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
        this.reactionRepository = reactionRepository;
        this.signUpFormRepository = signUpFormRepository;
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
                response.setParticipantId(participants.stream().filter(p -> p.getGroup().getId() == g.getId()).findFirst().orElseThrow(
                        () -> new ParticipantNotFountException("Participant not gffound")
                ).getId());
                list.add(response);
            });
            return list;
        });
    }

    public Future<Group> handleNewGroup(RequestNewGroup requestNewGroup, User user) {
        return this.execService.executeCallable(() -> {
            Group group = requestNewGroup.toGroup();
            group.setReaction(this.reactionRepository.findById(requestNewGroup.getReactionId()).orElseThrow(
                    () -> new ReactionNotFoundException("Reaction with given id doesnt exists!")
            ));
            group.setParticipantCount(1);
            group = this.groupRepository.save(group);

            Participant participant = requestNewGroup.toParticipant();
            participant.setGroup(group);
            participant.setUser(user);
            participant.setEnabled(true);
            if(group.isForm()) {
                SignUpForm form = requestNewGroup.toSignUpForm();
                form.setNickname("example");
                form.setGroup(group);
                System.out.println(form);
                System.out.println(this.signUpFormRepository.save(form));
            }
            participant = this.participantRepository.save(participant);
            group.setCreatorId(participant.getId());
            return this.groupRepository.save(group);
        });
    }

    public CompletableFuture<Participant> handleNewParticipant(InviteParticipant inviteParticipant, String id, User user) {
        return CompletableFuture.supplyAsync(
                () -> {
                    Participant participant = inviteParticipant.getParticipant();
                    Group group = this.groupRepository.findByInviteCode(id).orElseThrow(
                            () -> new GroupNotFoundException("Group with inviteCode "+ id+ " doesnt exists")
                    );
                    SignUpForm form = inviteParticipant.getSignUpForm();
                    form.setGroup(group);
                    participant.setGroup(group);
                    participant.setUser(user);
                    group.setParticipantCount(group.getParticipantCount() + 1);
                    this.groupRepository.save(group);
                    this.signUpFormRepository.save(form);
                    return this.participantRepository.save(participant);
                }, this.execService.getExecutor()
        );
//        return this.execService.executeCallable(() -> {
//            Participant participant = inviteParticipant.getParticipant();
//            Group group = GroupLight.fromGroupLight(inviteParticipant.getGroupLight());
//            SignUpForm form = inviteParticipant.getSignUpForm();
//            form.setGroup(group);
//            participant.setGroup(group);
//            participant.setUser(user);
//            group.setParticipantCount(group.getParticipantCount() + 1);
//            this.groupRepository.save(group);
//            this.signUpFormRepository.save(form);
//            return this.participantRepository.save(participant);
//        });
    }
}
