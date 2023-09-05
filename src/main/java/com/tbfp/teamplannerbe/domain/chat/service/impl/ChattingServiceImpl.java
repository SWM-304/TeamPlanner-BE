package com.tbfp.teamplannerbe.domain.chat.service.impl;

import com.tbfp.teamplannerbe.config.redis.util.ChattingRedisUtil;
import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto.ChattingReqeust;
import com.tbfp.teamplannerbe.domain.chat.service.ChattingService;
import com.tbfp.teamplannerbe.domain.chat.service.RedisMessageListener;
import com.tbfp.teamplannerbe.domain.chat.service.RedisPublisher;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Primary
@Transactional
@Service
public class ChattingServiceImpl implements ChattingService {
    private static final String CHATTING_ROOM = "CHATTING_ROOM";
    private static final String DELIMITER = ":";

    private final RedisPublisher redisPublisher;
    private final RedisMessageListener redisMessageListener;
    private final ChattingRedisUtil redisConnector;

    @Override
    public void sendMessage(Long chattingRoomId, ChattingReqeust chattingRequest) {
        log.info("[Service] sendMessage in");
        //redisMessageListener라는 객체로부터 chattingRoomId에 해당하는 토픽을 가져와서 해당 토픽으로 chattingRequest를 퍼블리시합니다.
        redisPublisher.publish(redisMessageListener.getTopic(chattingRoomId), chattingRequest);
        log.info("chatting is published to chatting room: {}", chattingRoomId);
        // Redis에 채팅 내용을 저장하는 코드입니다. CHATTING_ROOM과 DELIMITER를 합쳐서 특정 포맷의 키를 생성하고, 이 키를 사용하여 chattingRequest의 내용을 Redis에 저장합니다.
        redisConnector.saveChatting(CHATTING_ROOM + DELIMITER + chattingRoomId, chattingRequest.toChatting());
        log.info("chatting is saved");
    }
}
