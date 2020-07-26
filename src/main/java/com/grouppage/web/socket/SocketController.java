package com.grouppage.web.socket;

import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.service.ChatService;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;


@Controller
public class SocketController {

    private final ChatService chatService;
    private final GroupService groupService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public SocketController(ChatService chatService,
                            GroupService groupService,
                            SimpMessagingTemplate simpMessagingTemplate) {
        this.chatService = chatService;
        this.groupService = groupService;
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/conversation/{id}/sendmessage")
    public void sendMessage(
            @Payload SocketMessage socketMessage,
            @DestinationVariable long conversationId
            ) throws ExecutionException, InterruptedException {
        this.chatService.processNewPrivateMessage(socketMessage, conversationId, this.simpMessagingTemplate);
    }

    @MessageMapping("/group/{id}/sendpost")
    public SocketMessage newUser(
            @Payload SocketMessage socketMessage,
            @DestinationVariable long groupId
            ){
        this.chatService.processNewGroupPost(socketMessage, groupId, this.simpMessagingTemplate);
        return socketMessage;
    }
}
