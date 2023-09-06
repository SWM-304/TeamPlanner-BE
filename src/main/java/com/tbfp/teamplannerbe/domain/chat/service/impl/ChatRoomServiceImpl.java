package com.tbfp.teamplannerbe.domain.chat.service.impl;

import com.tbfp.teamplannerbe.config.redis.util.ChattingRedisUtil;
import com.tbfp.teamplannerbe.domain.chat.dto.response.*;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoomMember;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoom;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRoomMemberRepository;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRoomRepository;
import com.tbfp.teamplannerbe.domain.chat.service.ChatRoomService;
import com.tbfp.teamplannerbe.domain.chat.service.RedisMessageListener;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceImpl implements ChatRoomService {

    private static final String CHATTING_ROOM_KEY_HEADER = "CHATTING_ROOM:";
    private static final String dot = ".";
    private static final String TIME_DELIMITER = ":";


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberService memberService;
    private final RedisMessageListener redisMessageListener;
    private final ChattingRedisUtil redisConnector;


    /**
     * 채팅방 번호만 불러와줌
     */

    public List<ChatRoomResponseDto.ChatRoomListDto> getRoomList(String nickname) {
        log.info("모든 채팅방 리스트를 가져오는 곳");
//        redisMessageListener.enterChattingRoom(chattingRoomId);
        Member member = memberService.findMemberByNicknameOrElseThrowApplicationException(nickname);
        List<ChatRoomMember> chatRoomMemberList = member.getChatRoomMemberList();





        List<ChatRoomResponseDto.ChatRoomListDto> resultList = chatRoomMemberList.stream()
                .flatMap(crm ->
                        redisConnector.getMessages(CHATTING_ROOM_KEY_HEADER + crm.getChatRoom().getId()).stream()) // 모든 채팅 메시지를 하나의 스트림으로 펼칩니다.
                .collect(Collectors.groupingBy(
                        message -> message.getRoomId(),
                        Collectors.collectingAndThen(
                                Collectors.maxBy(Comparator.comparing(ChatMessage::getCreatedAt)),
                                maxMessage -> maxMessage.map(message -> {
                                    ChatRoomResponseDto.ChatRoomListDto result = ChatRoomResponseDto.ChatRoomListDto.builder()
                                            .roomId(message.getRoomId())
                                            .memberList(chatRoomMemberList.stream()
                                                    .flatMap(crm -> crm.getChatRoom().getChatRoomMemberList().stream()
                                                            .filter(c->c.getChatRoom().getId().equals(message.getRoomId()))
                                                            .map(c -> {
                                                                Map<String, String> memberInfo = new HashMap<>();
                                                                memberInfo.put("nickname", c.getMember().getNickname());
                                                                memberInfo.put("profileImage", c.getMember().getBasicProfile().getProfileImage()); // 프로필 이미지 필드 추가
                                                                return memberInfo;
                                                            }))
                                                    .collect(Collectors.toList()))
                                            .lastMessageTime(toCreatedTime(message.getCreatedAt()))
                                            .lastMessageText(message.getMessage())

                                            .build();
                                    return result;
                                }).orElse(null)
                        )
                ))
                .values()
                .stream()
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        return resultList;


//        return chatRoomMemberList.stream().map(ChatRoomResponseDto.ChatRoomListDto::toDto).
//                collect(Collectors.toList());
    }

    /**
     * 해당하는 채팅방에 대한 메세지까지 다 보여줌
     */


    public ChattingRoomDetailResponse getMyRoom(String nickname, Long chattingRoomId) {
        log.info("내 방에 들어와서 채팅을 작성하게 되면 실제 토픽을 생성해주는 곳");
        redisMessageListener.enterChattingRoom(chattingRoomId);
        ChatRoom chattingRoom = getChattingRoomById(chattingRoomId);
        return ChattingRoomDetailResponse.builder()
                .members(getMemberResponses(chattingRoom))
                .chattings(getChattingResponses(chattingRoomId))
                .build();
    }

    @Transactional
    public Long createRoom(String nickname, String targetNickname) {
        log.info("ChatRoomService.createRoom");
        log.info("nickname = " + nickname);
        log.info("targetNickname = " + targetNickname);
        Member member = memberService.findMemberByNicknameOrElseThrowApplicationException(nickname);
        log.info("member = " + member.getNickname());
        Member targetMember = memberService.findMemberByNicknameOrElseThrowApplicationException(targetNickname);
        log.info("targetMember = " + targetMember.getNickname());

        // 자기자신과 target이 같을 수 없다.
        if (member == targetMember) throw new ApplicationException(ApplicationErrorType.BAD_REQUEST);
        // 이미 존재하는 채팅방인지 확인
        boolean chatRoomExists = member.getChatRoomMemberList().stream()
                .anyMatch(chatRoomMember -> {
                    ChatRoom chatRoom = chatRoomMember.getChatRoom();
                    List<ChatRoomMember> chatRoomMemberList = chatRoom.getChatRoomMemberList();
                    return chatRoomMemberList.size() == 2
                            && chatRoomMemberList.stream().allMatch(
                            crm -> crm.getMember().equals(member) || crm.getMember().equals(targetMember)
                    );
                });

        if (chatRoomExists) {
            throw new ApplicationException(ApplicationErrorType.BAD_REQUEST);
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .build();
        chatRoomRepository.save(
                chatRoom
        );

        chatRoomMemberRepository.save(
                ChatRoomMember.builder()
                        .member(member)
                        .chatRoom(chatRoom)
                        .build()
        );
        chatRoomMemberRepository.save(
                ChatRoomMember.builder()
                        .member(targetMember)
                        .chatRoom(chatRoom)
                        .build()
        );
        redisMessageListener.enterChattingRoom(chatRoom.getId());
        return chatRoom.getId();
    }

    // 채팅 방이 존재하지는 여부
    public ChattingRoomCheckResponseDto chatRoomCheck(String nickname, String targetNickname) {


        Member member = memberService.findMemberByNicknameOrElseThrowApplicationException(nickname);
        log.info("member = " + member.getNickname());
        Member targetMember = memberService.findMemberByNicknameOrElseThrowApplicationException(targetNickname);
        log.info("targetMember = " + targetMember.getNickname());

        if (member == targetMember) throw new ApplicationException(ApplicationErrorType.BAD_REQUEST);
        // 이미 존재하는 채팅방인지 확인
        boolean chatRoomExists = member.getChatRoomMemberList().stream()
                .anyMatch(chatRoomMember -> {
                    ChatRoom chatRoom = chatRoomMember.getChatRoom();
                    List<ChatRoomMember> chatRoomMemberList = chatRoom.getChatRoomMemberList();
                    return chatRoomMemberList.size() == 2
                            && chatRoomMemberList.stream().allMatch(
                            crm -> crm.getMember().equals(member) || crm.getMember().equals(targetMember)
                    );
                });

        // false 시 이미 존재하는 채팅 방
        return ChattingRoomCheckResponseDto.builder()
                .roomCheck(!chatRoomExists)
                .build();
    }


    private ChatRoom getChattingRoomById(Long chattingRoomId) {
        log.info("해당하는 챗 id 가져오는 곳");
        return chatRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.UNAUTHORIZED));
    }
    private List<ChattingResponse> getChattingResponses(Long chattingRoomId) {
        log.info("redis 토픽안에 담긴 메세지를 가져오는 곳");

        return redisConnector.getMessages(CHATTING_ROOM_KEY_HEADER + chattingRoomId)
                .stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(message -> ChattingResponse.builder()
                        .senderId(message.getSenderId())
                        .content(message.getMessage())
                        .createdDate(toCreatedDate(message.getCreatedAt()))
                        .createdTime(toCreatedTime(message.getCreatedAt()))
                        .chatId(message.getId())
                        .build())
                .collect(Collectors.toList());
    }

    private List<MemberResponse> getMemberResponses(ChatRoom chattingRoom) {
        log.info("멤버 프로필 반환");

        return chattingRoom.getChatRoomMemberList()
                .stream()
                .map(chatRoomMember->MemberResponse.from(chatRoomMember.getMember()))
                .collect(Collectors.toList());
    }

    private String toCreatedTime(LocalDateTime createdAt) {
        return createdAt.getYear() + dot + createdAt.getMonthValue() + dot + createdAt.getDayOfMonth() + dot;
    }

    private String toCreatedDate(LocalDateTime createdAt) {
        return createdAt.getHour() + TIME_DELIMITER + createdAt.getMinute();
    }


    //채팅방 불러오기
//    public List<ChatRoom> findAllRoom() {
//        return chatRoomRepository.findAllRoom();
//    }
//
//    //채팅방 하나 불러오기
//    public ChatRoom findById(String roomId) {
//        return chatRoomRepository.findById(roomId);
//    }
//
//    //채팅방 생성
//    public ChatRoom createRoom(String name) {
//        return chatRoomRepository.createRoom(name);
//    }
//
//    public List<ChatRoom> findByNickname(String name) {
//        return chatRoomRepository.findByNickname(name);
//    }
}
