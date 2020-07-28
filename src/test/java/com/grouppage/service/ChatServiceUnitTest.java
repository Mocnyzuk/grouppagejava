package com.grouppage.service;

import com.grouppage.domain.repository.GroupRepository;
import com.grouppage.domain.repository.ParticipantRepository;
import com.grouppage.domain.repository.PostRepository;
import com.grouppage.domain.repository.chat.ConversationRepository;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.service.auth.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
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

    //void testHandleNewChat
}