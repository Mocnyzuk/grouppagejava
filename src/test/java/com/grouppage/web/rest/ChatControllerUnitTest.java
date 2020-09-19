package com.grouppage.web.rest;

import com.grouppage.domain.entity.Participant;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.domain.notmapped.Type;
import com.grouppage.domain.response.AddParticipantRequest;
import com.grouppage.exception.ParticipantNotFountException;
import com.grouppage.service.ChatService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ChatControllerUnitTest {

    @Mock
    private ChatService chatService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void startUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void shouldReturnNullWithStatusCreatedCreateConversation() throws ExecutionException, InterruptedException {
        Object response = this.chatController.saveFirstPrivMessage(
                new SocketMessage(1, "lala", Type.CHAT),
                "15"
        );
        assertNotNull(response);
        assertTrue(response instanceof ResponseEntity);
        assertEquals(HttpStatus.CREATED, ((ResponseEntity) response).getStatusCode());
    }
    @Test
    void shouldReturnNullWithStatusCreatedAddParticipant() throws ExecutionException, InterruptedException {
        Object response = this.chatController.addParticipantToConversation(
                new AddParticipantRequest(1, null)
        );
        assertNotNull(response);
        assertTrue(response instanceof ResponseEntity);
        assertEquals(HttpStatus.CREATED, ((ResponseEntity) response).getStatusCode());
    }
    @Test
    void shouldThrowException() throws ExecutionException, InterruptedException {
        doThrow(new ParticipantNotFountException("PArti doesnt exists")).when(chatService).addNewParticipantToConversation(any());
        try{
            this.chatController.addParticipantToConversation(
                    new AddParticipantRequest(1, null)
            );
        }catch (Exception e){
            assertTrue(e instanceof ParticipantNotFountException);
            return;
        }
        fail();
    }
}