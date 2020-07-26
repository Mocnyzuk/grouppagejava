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

//    @Autowired
//    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public SocketController(ChatService chatService,
                            GroupService groupService) {
        this.chatService = chatService;
        this.groupService = groupService;
    }

    @MessageMapping("/conversation/{id}/sendmessage")
//    @SendTo("/topic/test")
    public void sendMessage(
            @Payload SocketMessage socketMessage,
            @DestinationVariable String id
            ) throws ExecutionException, InterruptedException {
        this.chatService.processNewPrivateMessage(socketMessage, Long.parseLong(id));
        //this.simpMessagingTemplate.convertAndSend("/topic/test", socketMessage);
    }

    @MessageMapping("/group/{id}/sendpost")
    public void newPost(
            @Payload SocketMessage socketMessage,
            @DestinationVariable String id
            ) throws ExecutionException, InterruptedException {
        this.chatService.processNewGroupPost(socketMessage, Long.parseLong(id));
    }
}
