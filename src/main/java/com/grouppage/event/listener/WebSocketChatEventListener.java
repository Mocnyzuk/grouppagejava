package com.grouppage.event.listener;

import com.grouppage.domain.entity.chat.PrivateMessage;
import com.grouppage.service.auth.AuthService;
import com.grouppage.service.auth.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketChatEventListener {

    private final SimpMessageSendingOperations messagingTemplate;
    private final AuthService authService;

    @Autowired
    public WebSocketChatEventListener(SimpMessageSendingOperations messagingTemplate, AuthService authService) {
        this.messagingTemplate = messagingTemplate;
        this.authService = authService;
    }
    @EventListener
    public void handleWebSocketConnectListener(
            SessionConnectedEvent event){
        System.out.println("new conneciton appeared username: "+ event);
        StompHeaderAccessor headerAccessor =
                StompHeaderAccessor.wrap(event.getMessage());
        System.out.println(headerAccessor
                );

        //Principal principal = (Principal)((UsernamePasswordAuthenticationToken)event.getUser()).getPrincipal();
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
            System.out.println("DISCOENCTEd " + event.toString());
            System.out.println("user to +" + event.getUser());
            System.out.println("email to "+email);
        }
    }
}
