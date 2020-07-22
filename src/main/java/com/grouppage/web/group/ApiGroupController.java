package com.grouppage.web.group;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.response.GroupSearch;
import com.grouppage.domain.response.PostedPost;
import com.grouppage.exception.WrongDataPostedException;
import com.grouppage.service.group.ApiGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/group")
public class ApiGroupController {

    private final ApiGroupService apiGroupService;

    @Autowired
    public ApiGroupController(ApiGroupService apiGroupService) {
        this.apiGroupService = apiGroupService;
    }

//    @GetMapping
//    public ResponseEntity<List<GroupSearch>> searchGroups(
//            @RequestParam(name = "search") String phrase
//    ){
//        List<GroupSearch> response = apiGroupService.findGroupBySearchPhrase(phrase);
//        return ResponseEntity.ok(response);
//    }

    @GetMapping
    public ResponseEntity<List<Post>> getAllPosts(
            @RequestParam(name = "group") Group group
    ){
        List<Post> response = apiGroupService.getPostForGroupId(group);
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
        apiGroupService.handleNewPost(post, group);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
