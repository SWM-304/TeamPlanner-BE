package com.tbfp.teamplannerbe.domain.chat.service.impl;

import com.tbfp.teamplannerbe.config.redis.util.ChattingRedisUtil;
import com.tbfp.teamplannerbe.domain.chat.dto.response.*;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoomMember;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoom;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRepository;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRoomMemberRepository;
import com.tbfp.teamplannerbe.domain.chat.repository.ChatRoomRepository;
import com.tbfp.teamplannerbe.domain.chat.service.ChatRoomService;
import com.tbfp.teamplannerbe.domain.chat.service.pobsub.RedisMessageListener;
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

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ChatRoomServiceImpl implements ChatRoomService {

    private static final String dot = ".";
    private static final String TIME_DELIMITER = ":";


    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomMemberRepository chatRoomMemberRepository;
    private final MemberService memberService;
    private final RedisMessageListener redisMessageListener;
    private final ChattingRedisUtil redisConnector;
    private final ChatRepository chatRepository;


    /**
     * 채팅방 번호만 불러와줌
     */

    public List<ChatRoomResponseDto.ChatRoomListDto> getRoomList(String nickname) {
        log.info("모든 채팅방 리스트를 가져오는 곳");
//        redisMessageListener.enterChattingRoom(chattingRoomId);
        Member member = memberService.findMemberByNicknameOrElseThrowApplicationException(nickname);
        List<ChatRoomMember> chatRoomMemberList = member.getChatRoomMemberList();
        Map<Long, ChatRoomResponseDto.ChatRoomListDto> roomMap = new HashMap<>();

        chatRoomMemberList.forEach(chatRoomMember -> {
            Long roomId = chatRoomMember.getChatRoom().getId();
            List<ChatMessage> messages = chatRepository.readRoomWithChatMessageList(roomId);
            if (!messages.isEmpty()) {
                ChatMessage latestMessage = findLatestMessage(messages);

                if (latestMessage != null) {
                    List<Map<String, String>> memberInfoList = extractMemberInfoList(chatRoomMemberList, roomId);

                    ChatRoomResponseDto.ChatRoomListDto roomDto = createChatRoomListDto(roomId, memberInfoList, latestMessage);
                    roomMap.put(roomId, roomDto);
                }
            }
        });

        return new ArrayList<>(roomMap.values());
    }

    /**
     * 해당하는 채팅방에 대한 메세지까지 다 보여줌
     */


    public ChattingRoomDetailResponse getMyRoom(String nickname, Long chattingRoomId) {
        log.info("선택한 채팅방 정보를 보여주는 서비스 로직");


        Member member = memberService.findMemberByNicknameOrElseThrowApplicationException(nickname);
//         채팅방번호로 채팅방에 있는 MemberList 를 전부 가져옴
        List<ChatMessage> chatMessages = chatRepository.readRoomWithChatMessageList(chattingRoomId);

        // 내가 아닌 상대방이 해당 채팅방에 들어와 채팅을 확인하게 되면 ReadCount-=1

        List<ChatMessage> updateChatReadCount = chatMessages.stream()
                .filter(chatMember -> !Objects.equals(chatMember.getSenderId(), member.getId()) && chatMember.getReadCount() > 0)
                .map(chatMember -> {
                    chatMember.decreaseReadCount();
                    return chatMember;
                })
                .collect(Collectors.toList());


        chatRepository.saveAllChatMessage(updateChatReadCount);

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
        Member member = memberService.findMemberByNicknameOrElseThrowApplicationException(nickname);
        Member targetMember = memberService.findMemberByNicknameOrElseThrowApplicationException(targetNickname);

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
        Member targetMember = memberService.findMemberByNicknameOrElseThrowApplicationException(targetNickname);

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

    @Transactional
    public ChattingReadCountResponseDto readCountDecrease(String chatId){
        ChatMessage chatMessage=null;
        ChatMessage chat = chatRepository.findAllChatMessageListByChatId(chatId);

        if(chat.getReadCount()>0){
            chat.decreaseReadCount();
            chatMessage = chatRepository.saveChatMessageForReadCount(chat);
        }
        return ChattingReadCountResponseDto.builder()
                .readCount(chatMessage.getReadCount())
                .build();
    }


    private ChatMessage findLatestMessage(List<ChatMessage> messages) {
        return messages.stream()
                .max(Comparator.comparing(ChatMessage::getCreatedAt))
                .orElse(null);
    }

    private List<Map<String, String>> extractMemberInfoList(List<ChatRoomMember> chatRoomMemberList, Long roomId) {
        return chatRoomMemberList.stream()
                .flatMap(crm -> crm.getChatRoom().getChatRoomMemberList().stream())
                .filter(c -> c.getChatRoom().getId().equals(roomId))
                .map(c -> {
                    Map<String, String> memberInfo = new HashMap<>();
                    memberInfo.put("nickname", c.getMember().getNickname());
                    memberInfo.put("profileImage", c.getMember().getBasicProfile().getProfileImage());
                    return memberInfo;
                })
                .collect(Collectors.toList());
    }

    private ChatRoomResponseDto.ChatRoomListDto createChatRoomListDto(Long roomId, List<Map<String, String>> memberInfoList, ChatMessage latestMessage) {
        return ChatRoomResponseDto.ChatRoomListDto.builder()
                .roomId(roomId)
                .memberList(memberInfoList)
                .lastMessageTime(toCreatedTime(latestMessage.getCreatedAt()))
                .lastMessageText(latestMessage.getMessage())
                .readCount(latestMessage.getReadCount())
                .build();
    }

    private ChatRoom getChattingRoomById(Long chattingRoomId) {
        log.info("해당하는 챗 id 가져오는 곳");
        return chatRoomRepository.findById(chattingRoomId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.UNAUTHORIZED));
    }
    private List<ChattingResponse> getChattingResponses(Long chattingRoomId) {
        log.info("redis 토픽안에 담긴 메세지를 가져오는 곳");


        return chatRepository.readRoomWithChatMessageList(chattingRoomId)
                .stream()
                .sorted(Comparator.comparing(ChatMessage::getCreatedAt))
                .map(message -> ChattingResponse.builder()
                        .senderId(message.getSenderId())
                        .content(message.getMessage())
                        .createdDate(toCreatedDate(message.getCreatedAt()))
                        .createdTime(toCreatedTime(message.getCreatedAt()))
                        .chatId(message.getId())
                        .readCount(message.getReadCount())
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
}
