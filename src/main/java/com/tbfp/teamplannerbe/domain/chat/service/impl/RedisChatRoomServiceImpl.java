package com.tbfp.teamplannerbe.domain.chat.service.impl;

import com.tbfp.teamplannerbe.domain.chat.dto.redis.RedisChatRoom;
import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto;
import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto.ChattingReqeust;
import com.tbfp.teamplannerbe.domain.chat.dto.response.MemberResponse;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRepository;
import com.tbfp.teamplannerbe.domain.chat.repository.RedisChatRoomRepository;
import com.tbfp.teamplannerbe.domain.chat.service.RedisChatRoomService;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisMessageListener;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisPublisher;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class RedisChatRoomServiceImpl implements RedisChatRoomService {

    private static final Map<Long, ChannelTopic> TOPICS = new HashMap<>();


    private final RedisPublisher redisPublisher;
    private final RedisChatRoomRepository redisChatRoomRepository;
    private final MemberRepository memberRepository;
    private final ChatRepository chatRepository;
    @Override
    @Transactional
    public void connectChatRoom(Integer chatRoomNo, String username) {
        log.info("connectChatRoom");
        Optional<RedisChatRoom> existingChatRoom = redisChatRoomRepository.findByChatroomNoAndUsername(chatRoomNo, username);

        if (existingChatRoom.isEmpty()) {
            // 저장된 데이터가 없으면 새로운 RedisChatRoom 객체를 생성하고 저장합니다.
            RedisChatRoom redisChatRoom = RedisChatRoom.builder()
                    .username(username)
                    .chatroomNo(chatRoomNo)
                    .build();

            redisChatRoomRepository.save(redisChatRoom);
        }
    }

    @Override
    @Transactional
    public void disconnectChatRoom (Integer chatRoomNo, String username) {

        log.info("disconnectChatRoom");

        RedisChatRoom redisChatRoom = redisChatRoomRepository.findByChatroomNoAndUsername(chatRoomNo, username)
                .orElseThrow(IllegalStateException::new);

        redisChatRoomRepository.delete(redisChatRoom);
    }

    @Override
    public boolean isAllConnected(Integer chatRoomNo) {
        log.info("isAllConnected");

        List<RedisChatRoom> connectedList = redisChatRoomRepository.findByChatroomNo(chatRoomNo);
        System.out.println("isAllConnectedSize"+connectedList.size());
        return connectedList.size() == 2;
    }

    @Override
    public boolean isConnected(Integer chatRoomNo) {
        log.info("isConnected");

        List<RedisChatRoom> connectedList = redisChatRoomRepository.findByChatroomNo(chatRoomNo);
        return connectedList.size() >= 1;
    }


    // 읽지 않은 메시지 채팅장 입장시 읽음 처리
    @Transactional
    public void updateCountAllZero(Integer chatNo, String username) {
        log.info("updateCountAllZero");

        Member findMember = memberRepository.findByUsername(username)
                .orElseThrow(()->new ApplicationException(USER_NOT_FOUND));

        List<ChatMessage> chatList = chatRepository.findAllChatListByRoomId(chatNo);
        List<ChatMessage> filteredChatList = chatList.stream()
                .filter(chatMessage -> !chatMessage.getSenderId().equals(findMember.getId()))
                .collect(Collectors.toList());

        for (ChatMessage chatMessage : filteredChatList) {
            chatMessage.setReadCount(0);
        }
        chatRepository.saveAllChatMessage(chatList);
    }

    //현재 채팅방에 접속중인 회원이 있다면, 채팅 리스트 다시 서버에 요청해서 받도록 처리한다.

    @Override
    public void updateMessage(String username, Integer chatRoomNo) {
        log.info("updateMessage");
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        ChattingReqeust message = ChattingReqeust.builder()
                .senderId(member.getId())
                .chattingRoomId(Long.valueOf(chatRoomNo))
                .build();
        ChannelTopic topic = TOPICS.get(Long.valueOf(chatRoomNo));
        if (topic == null) {
            topic = new ChannelTopic(String.valueOf(chatRoomNo));
            TOPICS.put(Long.valueOf(chatRoomNo), topic);
        }
        System.out.println("토픽은:"+ TOPICS.get(Long.valueOf(chatRoomNo)));
        redisPublisher.publish(TOPICS.get(Long.valueOf(chatRoomNo)), message);
    }
}
