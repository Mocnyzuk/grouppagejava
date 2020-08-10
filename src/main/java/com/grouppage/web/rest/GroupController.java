package com.grouppage.web.rest;

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

import javax.validation.Valid;
import java.util.concurrent.ExecutionException;

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
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "nosort") String sort,
            @RequestParam(value = "member", required = false, defaultValue = "true") Boolean member
    ){
        Page<GroupLight> response = groupService.findGroupBySearchPhrase(phrase, size, page, sort, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @PathVariable long groupId,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "page", required = false, defaultValue = "0") Integer page,
            @RequestParam(value = "sort", required = false, defaultValue = "nosort") String sort
    ){
        Page<PostResponse> response = groupService.getPostForGroupId(groupId, page, size, sort);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<Void> handleNewPostInGroup (
            @RequestBody @Valid PostedPost post
    ) throws WrongDataPostedException, ExecutionException, InterruptedException {
        if((post == null) || (groupService.handleNewPost(post) == null)){
            throw new WrongDataPostedException("Posted Data doesnt work with our parser");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createNewGroup(
            @RequestBody @Valid RequestNewGroup requestNewGroup
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
            @RequestBody @Valid InviteParticipant participant
    ) {
        this.groupService.handleNewParticipant(participant, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
