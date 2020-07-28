package com.grouppage.service;

import com.grouppage.domain.entity.Group;
import com.grouppage.domain.entity.Reaction;
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
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.ReactivePageableHandlerMethodArgumentResolver;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@Disabled
class GroupServiceUnitTest {
    @Mock
    private AuthService authService;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private ParticipantRepository participantRepository;

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

        assertEquals(groupService.getPostForGroupId(group), Collections.EMPTY_LIST);
    }
}