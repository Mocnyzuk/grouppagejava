package com.grouppage.web.rest;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.User;
import com.grouppage.service.GroupService;
import com.grouppage.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

class GroupControllerUnitTest {
    
    @Mock
    private GroupService groupService;
    @Mock
    private AuthService authService;
    @InjectMocks
    private GroupController groupController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnPageOfPostForGivenRequest() {
        Group group = new Group();
        group.setId(1);
        group.setName("name");
        List<Post> posts = Arrays.asList(
                this.getTestPost(),
                this.getTestPost(),
                this.getTestPost(),
                this.getTestPost(),
                this.getTestPost(),
                this.getTestPost(),
                this.getTestPost(),
                this.getTestPost()

        );
        Page<Post> page = new PageImpl<Post>(posts, PageRequest.of(0, 20), 1);
        User user = new User();
        user.setId(1);
        doReturn(page)
                .when(groupService)
                .getPostForGroupId(1, 1, 1, "nic");
        when(authService.getUserFromContext()).thenReturn(user);

        Object result = this.groupController.getAllPosts(1, 1, 1, "nic");
        assertNotNull(result);
        assertTrue(result instanceof ResponseEntity);
        assertTrue(((ResponseEntity) result).getBody() instanceof Page);
        assertArrayEquals(posts.toArray(), ((Page) ((ResponseEntity) result).getBody()).getContent().toArray());
    }
    private Post getTestPost(){
        Post post = new Post();
        post.setContent("content");
        post.setId(new Random().nextLong());
        return post;
    }
}