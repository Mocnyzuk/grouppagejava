package com.grouppage.web.group;

import com.grouppage.domain.entity.Post;
import com.grouppage.domain.entity.chat.PrivateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class SocketGroupController {

    private final SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    public SocketGroupController(SimpMessagingTemplate simpMessagingTemplate) {
        this.simpMessagingTemplate = simpMessagingTemplate;
    }

    @MessageMapping("/group/{id}/newMessage")
    @SendTo("/topic/group/{id}")
    public Post sendMessage(
            @Payload Post post,
            @DestinationVariable String id
    ) {
        // TODO update lastOnline field in User
        return post;
    }

}
