package com.grouppage.web;

import com.grouppage.concurency.Process;
import com.grouppage.concurency.Value;
import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.service.ConcurrencyService;
import com.grouppage.service.auth.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;

    private final ParticipantRepository participantRepository;
    private final ConversationRepository conversationRepository;

    @Autowired
    public TestController(GroupRepository groupRepository, PostRepository postRepository, ParticipantRepository participantRepository, ConversationRepository conversationRepository) {
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Object>> getGroups() throws NoSuchMethodException, InterruptedException {




        Principal user = (Principal)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<Participant> participants = participantRepository.findAllByUserId(user.getId());
        List<Group> groups1 = participants.parallelStream()
                .map(Participant::getGroup)
                .collect(Collectors.toList());
        List<Participant> fromGroups = participantRepository.findAllByGroupIn(groups1);




        System.out.println(fromGroups.size());
        long time = System.currentTimeMillis();
        CountDownLatch latch = new CountDownLatch(5);
        ConcurrencyService con = new ConcurrencyService(
                new Process<>(
                latch,
                List.class,
                groupRepository,
                groupRepository.getClass().getMethod("findAll"),
                null),
                new Process<>(
                        latch,
                        List.class,
                        groupRepository,
                        groupRepository.getClass().getMethod("findAll"),
                        null),
                new Process<>(
                        latch,
                        List.class,
                        groupRepository,
                        groupRepository.getClass().getMethod("findAll"),
                        null),
                new Process<>(
                        latch,
                        List.class,
                        groupRepository,
                        groupRepository.getClass().getMethod("findAll"),
                        null),
                new Process<>(
                        latch,
                        List.class,
                        groupRepository,
                        groupRepository.getClass().getMethod("findAll"),
                        null));
        List<Object> groups = con.processTasks().stream().map(p-> p.getData()).collect(Collectors.toList());
        System.out.println(System.currentTimeMillis() - time);
        groups.stream().filter(Objects::nonNull).map(o -> ((List)o)).forEach(o -> System.out.println(o.size()));
        return ResponseEntity.ok(groups);
    }
    @GetMapping("/groupsno")
    public ResponseEntity<List<Group>> gre(){
        long time = System.currentTimeMillis();
        List<Group> groups = groupRepository.findAll();
        List<Group> groups1 = groupRepository.findAll();
        List<Group> groups2 = groupRepository.findAll();
        List<Group> groups3 = groupRepository.findAll();
        List<Group> groups4 = groupRepository.findAll();
        List<Group> result = new ArrayList<>(groups);
        result.addAll(groups1);
        result.addAll(groups2);
        result.addAll(groups3);
        result.addAll(groups4);
        System.out.println(System.currentTimeMillis() - time);
        System.out.println(groups.getClass());
        return ResponseEntity.ok(result);
    }
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(){
        return ResponseEntity.ok(postRepository.findAll());
    }
}
