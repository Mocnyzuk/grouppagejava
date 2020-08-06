package com.grouppage.web.rest;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.response.InviteParticipant;
import com.grouppage.domain.response.PostResponse;
import com.grouppage.domain.response.PostedPost;
import com.grouppage.domain.response.RequestNewGroup;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<Page<GroupLight>> searchGroups(
            @RequestParam(name = "search") String phrase,
            @RequestParam(value = "page", required = false, defaultValue = "0") String page,
            @RequestParam(value = "sort", required = false) String sort
    ){
        Page<GroupLight> response = groupService.findGroupBySearchPhrase(phrase, page, sort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @PathVariable long groupId,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort
    ){
        Page<PostResponse> response = groupService.getPostForGroupId(groupId, page, size, sort);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> handleNewPostInGroup (
            @RequestBody PostedPost post
    ) throws WrongDataPostedException, ExecutionException, InterruptedException {
        if((post == null) || (groupService.handleNewPost(post) == null)){
            throw new WrongDataPostedException("Posted Data doesnt work with our parser");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createNewGroup(
            @RequestBody RequestNewGroup requestNewGroup
    ){
        this.groupService.saveNewGroup(requestNewGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/invite")
    public ResponseEntity<GroupLight> checkIfCodeIsValid(
            @RequestParam(value = "id") String id
    ) {
        return ResponseEntity.ok(groupService.getGroupFromInviteCode(id));
    }
    @PostMapping("/invite/participant")
    public ResponseEntity<Void> inviteCodeAccess(
            @RequestParam(value = "id") String id,
            @RequestBody InviteParticipant participant
    ) {
        this.groupService.handleNewParticipant(participant, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
