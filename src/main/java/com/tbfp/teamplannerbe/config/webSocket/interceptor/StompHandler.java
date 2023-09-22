package com.tbfp.teamplannerbe.config.webSocket.interceptor;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.chat.service.ChatRoomService;
import com.tbfp.teamplannerbe.domain.chat.service.ChattingService;
import com.tbfp.teamplannerbe.domain.chat.service.RedisChatRoomService;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisMessageListener;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisSubscriber;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
public class StompHandler implements ChannelInterceptor {

    private final JwtProvider jwtProvider;
    private final RedisChatRoomService redisChatRoomService;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        if(accessor.getCommand() == StompCommand.CONNECT) {
            System.out.println("hello world"+accessor.getFirstNativeHeader("Authorization").replace("Bearer ",""));
            jwtProvider.verifyToken(accessor.getFirstNativeHeader("Authorization").replace("Bearer ",""));
            String username = jwtProvider.getUsernameFromToken(accessor.getFirstNativeHeader("Authorization").replace("Bearer ",""));
            handleMessage(accessor.getCommand(), accessor, username);
        }
        return message;
    }

    private void handleMessage(StompCommand stompCommand, StompHeaderAccessor accessor, String username) {

        log.info("handleMessage");
        switch (stompCommand) {

            case CONNECT:
                connectToChatRoom(accessor, username);
                break;
            case SUBSCRIBE:
            case SEND:
                jwtProvider.verifyToken(accessor.getFirstNativeHeader("Authorization").replace("Bearer ",""));
                break;
        }
    }

    private void connectToChatRoom(StompHeaderAccessor accessor, String email) {
//        // 채팅방 번호를 가져온다.
        log.info("connectToChatRoom");
        Integer chatRoomNo = getChatRoomNo(accessor);
        log.info("헤더에서 채팅방 번호 get"+chatRoomNo);

        // 채팅방 입장 처리 -> Redis에 입장 내역 저장
        redisChatRoomService.connectChatRoom(chatRoomNo, email);
        boolean isConnected = redisChatRoomService.isConnected(chatRoomNo);
        if(isConnected){
            // 읽지 않은 채팅을 전부 읽음 처리 redis가 아닌 dynamodb에서 update 시킴
            redisChatRoomService.updateCountAllZero(chatRoomNo, email);
        }

        // 현재 채팅방에 접속중인 인원이 있는지 확인한다.
        boolean isAllConnected = redisChatRoomService.isAllConnected(chatRoomNo);
        System.out.println("isConnected"+isAllConnected);
        if (isAllConnected) {
            redisChatRoomService.updateMessage(email, chatRoomNo);
        }

    }

    private Integer getChatRoomNo(StompHeaderAccessor accessor) {
        log.info("getChatRoomNo");
        return
                Integer.valueOf(
                        Objects.requireNonNull(
                                accessor.getFirstNativeHeader("chatRoomNo")
                        ));
    }


}