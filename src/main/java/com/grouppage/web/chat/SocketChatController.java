package com.grouppage.web.chat;

import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.repository.chat.PrivateMessageRepository;
import com.grouppage.service.chat.ApiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.stereotype.Controller;


@Controller
public class SocketChatController {

    private final ApiChatService apiChatService;

    @Autowired
    public SocketChatController(ApiChatService apiChatService) {
        this.apiChatService = apiChatService;
    }

    @MessageMapping("/conversation/{id}/sendMessage")
    @SendTo("/topic/conversation/{id}")
    public PrivateMessage sendMessage(
            @Payload PrivateMessage message
            ){
        return message;
    }

    @MessageMapping("/conversation/{id}/newUser")
    @SendTo("/topic/conversation/{id}")
    public PrivateMessage newUser(
            @Payload PrivateMessage privateMessage,
            SimpMessageHeaderAccessor headerAccessor
            ){
        headerAccessor.getSessionAttributes().put("nickname",
                privateMessage.getSender().getNickname());
        return privateMessage;
    }
}
