package com.grouppage.service;

import com.grouppage.domain.entity.*;
import com.grouppage.domain.logicForAsync.GroupLogicForAsync;
import com.grouppage.domain.notmapped.HashTag;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class GroupServiceUnitTest {
    @Mock
    private AuthService authService;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ParticipantRepository participantRepository;

    @Mock
    private GroupLogicForAsync groupLogicForAsync;

    @InjectMocks
    private GroupService groupService;

    @BeforeEach
    void name() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void getPostForGroup(){
        Group group = new Group();
        group.setParticipantCount(2);
        group.setReaction(new Reaction());
        group.setCreator_id(1);
        group.setForm(false);
        group.setPrivate(false);
        group.setCategory("IT");
        group.setDescription("IT");
        group.setAccept(false);
        group.setName("group");
        group.setId(2);

        when(groupRepository.findById(anyLong())).thenReturn(Optional.of(group));
        when(postRepository.findAllByGroup(any(Group.class))).thenReturn(Collections.EMPTY_LIST);

        assertEquals(Collections.EMPTY_LIST, Collections.EMPTY_LIST);
    }
    @Test
    void handlingNewPost(){
        fail("Methods not implemented yet");
    }
    @Test
    void findGroupBySearchPhrase(){
        fail("Methods not implemented yet");
    }

    @Test
    void testForUpVoting(){
        User user = new User();
        user.setId(1);
        Participant participant = new Participant(1, "marek", new ArrayList<>(), user, new Group(), true);
        Post post = new Post(1, new Group(), participant, "lalala", Arrays.asList(new HashTag("#lala")), 0);

        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));
        when(authService.getUserFromContext()).thenReturn(user);
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);
        int postResult = this.groupService.upVote(participant.getId(), post.getId());

        assertNotEquals(0, postResult);
    }
    @Test
    void testForDownVote(){
        User user = new User();
        user.setId(1);
        Post post = new Post(1, new Group(), null, "lalala", Arrays.asList(new HashTag("#lala")), 1);
        List<Post> liked = new ArrayList<>();
        liked.add(post);
        Participant participant = new Participant(1, "marek", liked, user, new Group(), true);

        when(participantRepository.findById(any())).thenReturn(Optional.of(participant));
        when(authService.getUserFromContext()).thenReturn(user);
        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        when(postRepository.save(any())).thenReturn(post);
        int postResult = this.groupService.downVote(participant.getId(), post.getId());

        assertNotEquals(1, postResult);
    }
}