package com.grouppage.web;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/test")
public class TestController {

    private final GroupRepository groupRepository;
    private final PostRepository postRepository;

    @Autowired
    public TestController(GroupRepository groupRepository, PostRepository postRepository) {
        this.groupRepository = groupRepository;
        this.postRepository = postRepository;
    }

    @GetMapping("/groups")
    public ResponseEntity<List<Group>> getGroups(){
        return ResponseEntity.ok(groupRepository.findAll());
    }
    @GetMapping("/posts")
    public ResponseEntity<List<Post>> getPosts(){
        return ResponseEntity.ok(postRepository.findAll());
    }
}
