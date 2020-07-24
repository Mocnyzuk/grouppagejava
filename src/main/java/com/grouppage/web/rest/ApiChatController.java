package com.grouppage.web.rest;

import com.grouppage.domain.response.PostedMessage;
import com.grouppage.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class ApiChatController {

    private final ChatService chatService;

    @Autowired
    public ApiChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/send")
    public ResponseEntity<Void> saveFirstPrivMessage(
            @RequestBody PostedMessage postedMessage,
            @RequestParam String receiver
    ){
        chatService.handleNewChat(postedMessage, receiver);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
