package com.kits.apichatapp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    @Override
    public void configureMessageBroker(MessageBrokerRegistry config){
        config.enableSimpleBroker("/topic"); // Carry Message back to Client with prefix "topic" have been subscribed
        config.setApplicationDestinationPrefixes("/app"); // Destination message from client to server
    }

    //registers prefix endpoint , enabling Spring STOMP support
    @Override
    public void registerStompEndpoints (StompEndpointRegistry registry){
        registry.addEndpoint("/chat");
        registry.addEndpoint("/chat").withSockJS(); // if WebSocket is not support all browsers
    }

}
