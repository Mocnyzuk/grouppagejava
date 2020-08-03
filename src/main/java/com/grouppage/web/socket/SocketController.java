package com.grouppage.web.socket;

import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.service.ChatService;
import com.grouppage.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.concurrent.ExecutionException;


@Controller
public class SocketController {

    private final ChatService chatService;
    private final GroupService groupService;

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
    }

    @MessageMapping("/group/{id}/sendpost")
    public void newPost(
            @Payload SocketMessage socketMessage,
            @DestinationVariable String id
            ) throws ExecutionException, InterruptedException {
        this.chatService.processNewGroupPost(socketMessage, Long.parseLong(id));
    }
}
