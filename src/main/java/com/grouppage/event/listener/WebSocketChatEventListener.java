package com.grouppage.event.listener;

import com.grouppage.domain.entity.chat.PrivateMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketChatEventListener {

    private final SimpMessageSendingOperations messagingTemplate;

    @Autowired
    public WebSocketChatEventListener(SimpMessageSendingOperations messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }
    @EventListener
    public void handleWebSocketConnectListener(
            SessionConnectedEvent event){
        System.out.println("received new web socket connection "+ event.getUser().getName());
    }
    @EventListener
    public void handleWebSocketDisconnectListener(
            SessionDisconnectEvent event
    ){
        StompHeaderAccessor headerAccessor =
                StompHeaderAccessor.wrap(event.getMessage());
        String email = (String) headerAccessor
                .getSessionAttributes()
                .get("nickname");
        if(null != email){
            PrivateMessage message = new PrivateMessage();
            message.setType("LEAVE");
            // TODO insert sender and receiver to message by JWT or some others tool
            message.setSender(null);
            messagingTemplate.convertAndSend("/topic/public",
                    message);
        }
    }
}
