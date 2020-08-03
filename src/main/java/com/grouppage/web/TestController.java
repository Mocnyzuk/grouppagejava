package com.grouppage.web;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.service.ExecService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;
    private final ExecService execService;

    private final ParticipantRepository participantRepository;
    private final ConversationRepository conversationRepository;

    @Autowired
    public TestController(GroupRepository groupRepository, PostRepository postRepository,ExecService execService, ParticipantRepository participantRepository, ConversationRepository conversationRepository) {
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
        this.execService = execService;
        this.participantRepository = participantRepository;
        this.conversationRepository = conversationRepository;
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getGroups() {
        List<Group> groups = this.getByExec();
        return ResponseEntity.ok(groups);
    }
    @GetMapping("/convs")
    public ResponseEntity<List<Conversation>> getConsv() {
        return ResponseEntity.ok(this.conversationRepository.findAll());
    }

    private List<Group> getByExec(){
        Future<List<Group>> group1 = execService.executeCallable(groupRepository::findAll);
        Future<List<Group>> group2 = execService.executeCallable(groupRepository::findAll);
        Future<List<Group>> group3 = execService.executeCallable(groupRepository::findAll);
        Future<List<Group>> group4 = execService.executeCallable(groupRepository::findAll);
        Future<List<Group>> group5 = execService.executeCallable(groupRepository::findAll);
        try {
            List<Group> result = new ArrayList<>(group1.get());
            result.addAll(group2.get());
            result.addAll(group3.get());
            result.addAll(group4.get());
            result.addAll(group5.get());
            return result;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
//    @GetMapping("/groupsno")
//    public ResponseEntity<List<Group>> gre(){
//        long time = System.currentTimeMillis();
//        List<Group> groups = groupRepository.findAll();
//        List<Group> groups1 = groupRepository.findAll();
//        List<Group> groups2 = groupRepository.findAll();
//        List<Group> groups3 = groupRepository.findAll();
//        List<Group> groups4 = groupRepository.findAll();
//        List<Group> result = new ArrayList<>(groups);
//        result.addAll(groups1);
//        result.addAll(groups2);
//        result.addAll(groups3);
//        result.addAll(groups4);
//        System.out.println(System.currentTimeMillis() - time);
//        System.out.println(groups.getClass());
//        return ResponseEntity.ok(result);
//    }
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(){
        return ResponseEntity.ok(postRepository.findAll());
    }
}
