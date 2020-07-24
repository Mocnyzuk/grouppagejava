package com.grouppage.web.socket;

import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.service.ChatService;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.net.Socket;
import java.util.ArrayList;


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

    @MessageMapping("/conversation/{id}/sendMessage")
    public void sendMessage(
            @Payload SocketMessage socketMessage
            ){
        SocketMessage resposne = this.chatService.processNewPrivateMessage(socketMessage, simpMessagingTemplate);
        //simpMessagingTemplate.convertAndSend("/topic/{userId}", socketMessage);
    }

    @MessageMapping("/group/{id}/newPost")
    @SendTo("/topic/conversation/{id}")
    public SocketMessage newUser(
            @Payload SocketMessage socketMessage
            ){
//        groupService.handleNewPostFromSocket(socketMessage);
        return socketMessage;
    }
}
