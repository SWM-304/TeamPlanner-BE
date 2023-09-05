package com.tbfp.teamplannerbe.config.webSocket;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer { // (1)

    //    private final StompHandler stompHandler;
    private final StompHandler stompHandler; // jwt 인증


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) { // (5)
//        registry.addEndpoint("/stomp/chat") // ex ) ws://localhost:9000/stomp/chat
//                .setAllowedOriginPatterns("*").withSockJS();
        registry.addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");
    }
    // 새로 추가
    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
//    @Order(Ordered.HIGHEST_PRECEDENCE + 99)
//    public class FilterChannelInterceptor implements ChannelInterceptor { }


//    @Override // (6)
//    public void configureClientInboundChannel (ChannelRegistration registration){
//        registration.interceptors(stompHandler);
//    }
}