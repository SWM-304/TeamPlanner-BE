package com.tbfp.teamplannerbe.domain.chat.service;

import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto.ChattingReqeust;
import com.tbfp.teamplannerbe.domain.common.util.JsonParser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class RedisPublisher {

    private final RedisTemplate<String, String> redisTemplate;
    private final JsonParser jsonParser;

    public void publish(ChannelTopic topic, ChattingReqeust chattingRequest) {
        log.info("퍼블리시 하는 곳");

        redisTemplate.convertAndSend(topic.getTopic(), jsonParser.toJson(chattingRequest));
    }
}