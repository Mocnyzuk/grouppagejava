package com.grouppage.web.rest;

import com.grouppage.domain.notmapped.GroupForm;
import com.grouppage.domain.notmapped.ParticipantLight;
import com.grouppage.domain.notmapped.SignUpFormLight;
import com.grouppage.domain.response.*;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
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
    public ResponseEntity<Page<GroupSearch>> searchGroups(
            @RequestParam(name = "search") String phrase,
            @RequestParam(value = "page", required = false, defaultValue = "1") String page,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "sort", required = false, defaultValue = "nosort") String sort,
            @RequestParam(value = "member", required = false, defaultValue = "false") Boolean member
    ){
        Page<GroupSearch> response = groupService.findGroupBySearchPhrase(phrase, size, page, sort, member);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Page<PostResponse>> getAllPosts(
            @PathVariable long groupId,
            @RequestParam(value = "size", required = false, defaultValue = "20") Integer size,
            @RequestParam(value = "page", required = false, defaultValue = "1") Integer page,
            @RequestParam(value = "sort", required = false, defaultValue = "nosort") String sort
    ){
        Page<PostResponse> response = groupService.getPostForGroupId(groupId, page, size, sort);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/{groupId}/forms")
    public ResponseEntity<List<SignUpFormLight>> getAllForms(
            @PathVariable("groupId") String groupId
    ) {
        return ResponseEntity.ok(this.groupService.getAllSignUpForms(Long.parseLong(groupId)));
    }
    @PostMapping("/{groupId}/forms")
    public ResponseEntity<Void> acceptForms(
            @PathVariable("groupId") String groupId,
            @RequestBody String[] nicknames
    ){
        this.groupService.acceptThisParticipants(nicknames, Long.parseLong(groupId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/{groupId}/edit")
    public ResponseEntity<Void> editGroup(
            @PathVariable("groupId") String groupId,
            @RequestBody Map<String, String> map
    ){
        this.groupService.editGroup(map, Long.parseLong(groupId)) ;
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{groupId}/participants")
    public ResponseEntity<List<ParticipantLight>> getAllParticipants(
            @PathVariable(name = "groupId") long groupId
    ){
        return ResponseEntity.ok(this.groupService.getAllParticipants(groupId));
    }

    @DeleteMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(
            @PathVariable(name = "groupId")String groupId
    ){
        this.groupService.removeMeFromGroup(Long.parseLong(groupId));
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping
    public ResponseEntity<Void> handleNewPostInGroup (
            @RequestBody @Valid PostedPost post
    ) throws WrongDataPostedException, ExecutionException, InterruptedException {
        if(post == null){
            throw new WrongDataPostedException("Posted Data doesnt work with our parser");
        }
        this.groupService.handleNewPost(post);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createNewGroup(
            @RequestBody @Valid RequestNewGroup requestNewGroup
    ){
        System.out.println(requestNewGroup);
        this.groupService.saveNewGroup(requestNewGroup);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @GetMapping("/invite")
    public ResponseEntity<GroupForm> checkIfCodeIsValid(
            @RequestParam(value = "id") String id
    ) {
        return ResponseEntity.ok(groupService.getGroupFromInviteCode(id));
    }
    @PostMapping("/invite/participant")
    public ResponseEntity<Void> inviteCodeAccess(
            @RequestParam(value = "id") String id,
            @RequestBody @Valid Map<String, String> groupForm
    ) {
        this.groupService.handleNewParticipant(groupForm, id);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
