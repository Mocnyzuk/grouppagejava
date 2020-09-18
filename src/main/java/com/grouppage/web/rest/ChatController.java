package com.grouppage.web.rest;

import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.domain.notmapped.ConversationLight;
import com.grouppage.domain.notmapped.ConversationMessage;
import com.grouppage.domain.notmapped.PrivateMessageForGet;
import com.grouppage.domain.notmapped.SocketMessage;
import com.grouppage.domain.response.AddParticipantRequest;
import com.grouppage.service.ChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/api/message")
public class ChatController {

    private final ChatService chatService;

    @Autowired
    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @PostMapping("/new")
    public ResponseEntity<Void> saveFirstPrivMessage(
            @RequestBody @Valid SocketMessage socketMessage,
            @RequestParam String receiver
    ) throws ExecutionException, InterruptedException {
        chatService.handleNewChat(socketMessage, receiver);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PostMapping("/add")
    public ResponseEntity<Void> addParticipantToConversation(
            @RequestBody @Valid AddParticipantRequest request
    ) throws ExecutionException, InterruptedException {
        this.chatService.addNewParticipantToConversation(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationLight>> getConversationList(){
        return ResponseEntity.ok(this.chatService.getConversations());
    }
    @GetMapping("/{conversationId}")
    public ResponseEntity<List<PrivateMessageForGet>> getAllMessagesFromConversation(
            @PathVariable("conversationId") String conversationId
    ){
        return ResponseEntity.ok(this.chatService.getAllMessages(Long.parseLong(conversationId)));
    }
}
