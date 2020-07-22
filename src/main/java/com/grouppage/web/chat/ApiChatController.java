package com.grouppage.web.chat;

import com.grouppage.domain.response.PostedMessage;
import com.grouppage.service.chat.ApiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/message")
public class ApiChatController {

    private final ApiChatService apiChatService;

    @Autowired
    public ApiChatController(ApiChatService apiChatService) {
        this.apiChatService = apiChatService;
    }

    @PostMapping("/{groupId}")
    public ResponseEntity<Void> saveFirstPrivMessage(
            @RequestBody PostedMessage postedMessage,
            @RequestParam String receiver
    ){
        if(!receiver.isEmpty() && null != postedMessage){
            apiChatService.handleNewChat(postedMessage, receiver);
        }
        return ResponseEntity.ok().build();
    }
}
