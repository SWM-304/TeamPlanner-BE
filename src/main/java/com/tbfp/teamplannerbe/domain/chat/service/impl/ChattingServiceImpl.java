package com.tbfp.teamplannerbe.domain.chat.service.impl;

import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto.ChattingReqeust;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRepository;
import com.tbfp.teamplannerbe.domain.chat.service.ChattingService;
import com.tbfp.teamplannerbe.domain.chat.service.RedisChatRoomService;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisMessageListener;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisPublisher;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;

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
    private final ChatRepository chatRepository;
    private final MemberRepository memberRepository;
    private final RedisChatRoomService redisChatRoomService;

    @Override
    public void sendMessage(Long chattingRoomId, ChattingReqeust chattingRequest) {
        log.info("[Service] sendMessage in");

        boolean isConnectedAll = redisChatRoomService.isAllConnected(Math.toIntExact(chattingRoomId));

        Integer readCount = isConnectedAll ? 0 : 1;


        String createdUUID = chatRepository.saveChatMessage(chattingRequest.toChatting());
        chattingRequest.setId(createdUUID);
        chattingRequest.setReadCount(readCount);
        redisPublisher.publish(redisMessageListener.getTopic(chattingRoomId), chattingRequest);
        log.info("chatting is published to chatting room: {}", chattingRoomId);
        // CHATTING_ROOM과 DELIMITER를 합쳐서 특정 포맷의 키를 생성하고, 이 키를 사용하여 chattingRequest의 내용을 Redis에 저장
//        redisConnector.saveChatting(CHATTING_ROOM + DELIMITER + chattingRoomId, chattingRequest.toChatting());
        log.info("chatting is saved");
    }
}
