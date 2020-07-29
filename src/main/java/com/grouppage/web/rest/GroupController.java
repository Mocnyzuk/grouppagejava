package com.grouppage.web.rest;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.response.PostedPost;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group")
public class GroupController {

    private final GroupService groupService;

    private final int pageSize;

    @Autowired
    public GroupController(GroupService groupService,
                           @Value("${custom.default.page.size}") int pageSize) {
        this.groupService = groupService;
        this.pageSize = pageSize;
    }

//    @GetMapping
//    public ResponseEntity<List<GroupSearch>> searchGroups(
//            @RequestParam(name = "search") String phrase
//    ){
//        List<GroupSearch> response = apiGroupService.findGroupBySearchPhrase(phrase);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping("/{groupId}")
    public ResponseEntity<Page<List<Post>>> getAllPosts(
            @PathVariable long groupId,
            @RequestParam(value = "size", required = false) Integer size,
            @RequestParam(value = "page", required = false) Integer page,
            @RequestParam(value = "sort", required = false) String sort
    ){
        Page<List<Post>> response = groupService.getPostForGroupId(groupId, page, size, sort);
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

}
