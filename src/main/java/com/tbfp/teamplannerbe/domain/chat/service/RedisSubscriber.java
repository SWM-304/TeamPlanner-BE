package com.tbfp.teamplannerbe.domain.chat.service;

import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto;
import com.tbfp.teamplannerbe.domain.common.util.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@RequiredArgsConstructor
@Service
public class RedisSubscriber implements MessageListener {

    private final JsonParser jsonParser;
    private final RedisTemplate redisTemplate;
    private final SimpMessageSendingOperations messagingTemplate;
    private static final String DOT = ".";
    private static final String TIME_DELIMITER = ":";

    @Override
    public void onMessage(Message message, byte[] pattern) {
        ChatRoomRequestDto.ChattingReqeust chattingRequest = jsonParser.toChattingRequest((String) redisTemplate.getStringSerializer().deserialize(message.getBody()));
        chattingRequest.setCreatedDate(toCreatedDate(LocalDateTime.now())); // Set the current timestamp
        chattingRequest.setCreatedTime(toCreatedTime(LocalDateTime.now())); // Set the current timestamp
        messagingTemplate.convertAndSend("/sub/chattings/rooms/" + chattingRequest.getChattingRoomId(), chattingRequest);
        log.info("Message [{}] send by member: {} to chatting room: {}", chattingRequest.getContent(), chattingRequest.getSenderId(), chattingRequest.getChattingRoomId());
    }


    private String toCreatedTime(LocalDateTime createdAt) {
        return createdAt.getYear() + DOT + createdAt.getMonthValue() + DOT + createdAt.getDayOfMonth() + DOT;
    }

    private String toCreatedDate(LocalDateTime createdAt) {
        return createdAt.getHour() + TIME_DELIMITER + createdAt.getMinute();
    }




}
