package com.kits.apichatapp.config;

import com.kits.apichatapp.model.ChatMessage;
import com.kits.apichatapp.model.MessageType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private SimpMessageSendingOperations messageTemplate; // send message to CLIENT through STOMP

    // @EventListener active when every event occurs
    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) { // SessionDisconnectEvent: handle session client connect WebSocket disconnect
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage()); //StompHeaderAccessor: interact with Header through STOMP
        String username = (String) headerAccessor.getSessionAttributes().get("username");
        if(username != null) {
            log.info("User disconnected: {}", username);
            var message = ChatMessage.builder()
                    .type(MessageType.LEAVE)
                    .sender(username)
                    .build();
            messageTemplate.convertAndSend("/topic/public", message); // send to destination public inform client left out of group
        }
    }
}