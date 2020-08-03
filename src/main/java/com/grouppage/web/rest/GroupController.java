package com.grouppage.web.rest;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.notmapped.GroupLight;
import com.grouppage.domain.response.InviteParticipant;
import com.grouppage.domain.response.PostedPost;
import com.grouppage.domain.response.RequestNewGroup;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;



    @Autowired
    public GroupController(GroupService groupService) {
        this.groupService = groupService;
    }

    @GetMapping
    public ResponseEntity<Page<Group>> searchGroups(
            @RequestParam(name = "search") String phrase,
            @RequestParam(value = "sort", required = false) String sort
    ){
        Page<Group> response = groupService.findGroupBySearchPhrase(phrase, sort);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Page<Post>> getAllPosts(
            @PathVariable long groupId,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort
    ){
        Page<Post> response = groupService.getPostForGroupId(groupId, page, size, sort);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> handleNewPostInGroup (
            @RequestParam(name = "group") Group group,
            @RequestBody PostedPost post
    )throws WrongDataPostedException {
        if(post == null){
            throw new WrongDataPostedException("Posted Data doesnt work with our parser");
        }
        groupService.handleNewPost(post, group);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createNewGroup(
            @RequestBody RequestNewGroup requestNewGroup
    ){
        groupService.saveNewGroup(requestNewGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/invite/participant")
    public ResponseEntity<Void> inviteCodeAccess(
            @RequestParam(value = "id") String id,
            @RequestBody InviteParticipant participant
    ){
        groupService.handleNewParticipant(participant, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/invite")
    public ResponseEntity<GroupLight> checkIfCodeIsValid(
            @RequestParam(value = "id") String id
    ) {
        return ResponseEntity.ok(groupService.getGroupFromInviteCode(id));
    }
}
