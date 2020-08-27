package com.grouppage.service;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.entity.User;
import com.grouppage.domain.entity.chat.Conversation;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.domain.notmapped.Type;
import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
@Disabled
class ChatServiceUnitTest {
    @Mock
    private ParticipantRepository participantRepository;
    @Mock
    private ConversationRepository conversationRepository;
    @Mock
    private PrivateMessageRepository privateMessageRepository;
    @Mock
    private GroupRepository groupRepository;
    @Mock
    private PostRepository postRepository;
    @Mock
    private SimpMessagingTemplate simpMessagingTemplate;
    @Mock
    private AuthService authService;

    @Mock
    private ExecService execService;

    @InjectMocks
    private ChatService chatService;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testHandleNewChat(){
        User user = new User();
        user.setId(1);

        Participant participant = new Participant();
        participant.setLikedPosts(new ArrayList<>());
        participant.setUser(user);
        participant.setNickname("janek");
        participant.setId(1);

        Conversation conversation = new Conversation();
        conversation.setParticipants(Arrays.asList(participant, participant));

        String receiver = "12";

        SocketMessage socketMessage =
                new SocketMessage(1, "lalala", Type.CHAT);

        when(participantRepository.findById(participant.getId())).thenReturn(Optional.of(participant));
        when(participantRepository.findById(Long.parseLong(receiver))).thenReturn(Optional.of(participant));
        when(conversationRepository.save(any())).thenReturn(conversation);
        when(conversationRepository.findById(any())).thenReturn(Optional.of(conversation));




    }

}