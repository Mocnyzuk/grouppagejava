package com.grouppage.web.rest;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.exception.PostNotFoundException;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.UsesJava8;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/post")
public class PostController {

    private final GroupService groupService;

    @Autowired
    public PostController(GroupService groupService) {
        this.groupService = groupService;
    }

    @PostMapping("/upVote/{participant}")
    public ResponseEntity<Post> upVote(
            @PathVariable Participant participant,
            @RequestParam(name = "id") long postId
    ) throws PostNotFoundException {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.groupService.upVote(participant, postId));
    }
    @PostMapping("/removeVote/{participant}")
    public ResponseEntity<Post> removeVote(
            @PathVariable Participant participant,
            @RequestParam(name = "id") long postId
    )throws PostNotFoundException{
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(this.groupService.downVote(participant, postId));
    }
}
