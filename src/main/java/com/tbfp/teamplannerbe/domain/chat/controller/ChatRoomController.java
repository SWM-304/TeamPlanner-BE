package com.tbfp.teamplannerbe.domain.chat.controller;

import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.tbfp.teamplannerbe.domain.auth.MemberDetails;
import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import com.tbfp.teamplannerbe.domain.chat.service.RedisChatRoomService;
import com.tbfp.teamplannerbe.domain.chat.service.impl.ChatRoomServiceImpl;
import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatRoomController {
    private final ChatRoomServiceImpl chatRoomService;
    private final RedisChatRoomService redisChatRoomService;

    /**
     * 선택 한 채팅방에대한 채팅을 뿌려주는 곳!
     */

    @GetMapping("/room/{chattingRoomId}/{id}/{createdAt}")
    public ResponseEntity getMyRoom(@PathVariable Long chattingRoomId,
                                    @PathVariable(required = false) String id,
                                    @PathVariable(required = false) String createdAt,
                                    @AuthenticationPrincipal MemberDetails memberDetails) {
        log.info("getMyRoom Controller 호출");
        Map<String, AttributeValue> exclusiveStartKey = new HashMap<>();
        if(chattingRoomId!=null && createdAt!=null && id!=null){
            ChatMessage chatMessage=new ChatMessage();
            exclusiveStartKey = chatMessage.entityToDynomodb(chattingRoomId, id, createdAt);
        }
        return ResponseEntity.ok(
                chatRoomService.getMyRoom(memberDetails.getNickname(),chattingRoomId,exclusiveStartKey)

        );
    }

    /**
     * 각 유저의 채팅방리스트를 뿌려주는 곳!
     */
    @GetMapping("/room")
    public ResponseEntity getMyRoomList(@AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity.ok(
                chatRoomService.getRoomList(memberDetails.getNickname())
        );
    }
    @GetMapping("/room-check/{targetnickname}")
    public ResponseEntity isRoomCheck(@PathVariable String targetnickname,@AuthenticationPrincipal MemberDetails memberDetails){
        return ResponseEntity.ok(
                chatRoomService.chatRoomCheck(memberDetails.getNickname(),targetnickname));

    }

    /**
     * 채팅방을 새로 생성해주는곳!
     */

    @PostMapping("/room")
    public ResponseEntity createRoom(@AuthenticationPrincipal MemberDetails memberDetails, @RequestBody ChatRoomRequestDto.CreateRoomDto createRoomDto) {
        return ResponseEntity.ok(
                chatRoomService.createRoom(memberDetails.getNickname(), createRoomDto.getTargetNickname())
        );
    }
    @GetMapping("/{chatId}")
    public ResponseEntity checkSubscribeThenReadCountDecrease(@PathVariable String chatId) {
        return ResponseEntity.ok(
                chatRoomService.readCountDecrease(chatId)
        );
    }

    // 채팅방 접속 끊기
    @PostMapping("/chatroom/{chatroomNo}")
    public ResponseEntity disconnectChat(@PathVariable("chatroomNo") Integer chatroomNo,
                                         Principal principal) {
        System.out.println("채팅방 접속 끊기"+chatroomNo);
        redisChatRoomService.disconnectChatRoom(chatroomNo, principal.getName());
        return ResponseEntity.ok("채팅연결해제");
    }
}
