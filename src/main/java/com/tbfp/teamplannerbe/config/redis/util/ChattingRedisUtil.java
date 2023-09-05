package com.tbfp.teamplannerbe.config.redis.util;


import com.amazonaws.services.apigateway.model.BadRequestException;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChattingRedisUtil {
    private static final String NO_MESSAGE = "";

    private final RedisTemplate<String, Object> redisTemplate;

    public void set(String key, String value, Duration expireTime) {
        ValueOperations<String, Object> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, value, expireTime);
    }

    public String get(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.UNAUTHORIZED));
    }

    public void saveChatting(String chattingRoom, ChatMessage chatting) {
        HashOperations<String, String, ChatMessage> hashOperations = redisTemplate.opsForHash();
        hashOperations.put(chattingRoom, chatting.getId(), chatting);
        log.info("create new chatting [{}] int chatting room '{}'", chatting, chattingRoom);
    }

    public String getLastMessage(String chattingRoom) {
        HashOperations<String, String, ChatMessage> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(chattingRoom)
                .stream()
                .max(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(ChatMessage::getMessage)
                .orElse(NO_MESSAGE);
    }

    public String getUsernameByRefreshToken(String key) {
        return Optional.ofNullable(redisTemplate.opsForValue().get(key))
                .map(String::valueOf)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.REFRESH_TOKEN_FOR_USER_NOT_FOUND));
    }

    public void delete(String key) {
        redisTemplate.delete(key);
    }

    public List<ChatMessage> getMessages(String chattingRoomKey) {
        HashOperations<String, String, ChatMessage> hashOperations = redisTemplate.opsForHash();
        return hashOperations.values(chattingRoomKey);
    }
}
