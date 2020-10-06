package com.grouppage.web.socket;

import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.service.ChatService;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.sockjs.transport.SockJsSession;

import java.util.concurrent.ExecutionException;


@Controller
public class SocketController {

    private final ChatService chatService;

    @Autowired
    public SocketController(ChatService chatService) {
        this.chatService = chatService;
    }

    @MessageMapping("/conversation/{id}/sendmessage")
//    @SendTo("/topic/test")
    public void sendMessage(
            @Payload SocketMessage socketMessage,
            @DestinationVariable String id
            ) {
        System.out.println(socketMessage);
        this.chatService.processNewPrivateMessage(socketMessage, Long.parseLong(id));
    }

    @MessageMapping("/conversation/new/{participantId}")
    public void newConversation(
            @Payload SocketMessage socketMessage,
            @DestinationVariable String participantId
    ) throws ExecutionException, InterruptedException {
        this.chatService.handleNewChat(socketMessage, participantId);
    }
    @MessageMapping("/group/{id}/sendpost")
    public void newPost(
            @Payload SocketMessage socketMessage,
            @DestinationVariable String id
            ) throws ExecutionException, InterruptedException {
        this.chatService.processNewGroupPost(socketMessage, Long.parseLong(id));
    }
}
