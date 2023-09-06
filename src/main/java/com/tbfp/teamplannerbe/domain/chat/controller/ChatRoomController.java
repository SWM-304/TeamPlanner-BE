package com.tbfp.teamplannerbe.domain.chat.controller;

import com.tbfp.teamplannerbe.domain.auth.MemberDetails;
import com.tbfp.teamplannerbe.domain.chat.service.impl.ChatRoomServiceImpl;
import com.tbfp.teamplannerbe.domain.chat.dto.request.ChatRoomRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat")
@Slf4j
public class ChatRoomController {
    private final ChatRoomServiceImpl chatRoomService;

    /**
     * 선택 한 채팅방에대한 채팅을 뿌려주는 곳!
     */

    @GetMapping("/room/{chattingRoomId}")
    public ResponseEntity getMyRoom(@PathVariable Long chattingRoomId,@AuthenticationPrincipal MemberDetails memberDetails) {
        return ResponseEntity.ok(
                chatRoomService.getMyRoom(memberDetails.getNickname(),chattingRoomId)

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

//    /**
//     *
//     * 이건뭐지 ?
//     */
//    @GetMapping("/room/{roomId}/message")
//    public ResponseEntity getRoomMessage(@AuthenticationPrincipal MemberDetails memberDetails) {
//        return ResponseEntity.ok("");
//    }

//    @PostMapping("/room")
//    public ResponseEntity createRoom(AuthenticatedPrincipal authenticatedPrincipal, String targetUserNickname) {
//
//    }




    // 내가 들어가 있는 채팅방 목록
//    @GetMapping("/room")
//    public List<ChatRoom> room(Principal principal) {
//        String name = principal.getName();
//        System.out.println("principal.getName() = " + name);
//        return chatRoomService.findByNickname(name);
//    }
//
//    // 채팅방 생성 (첫 메시지 보내면서 생성)
//    @PostMapping("/room")
//    @ResponseBody
//    public ChatRoom createRoom(@RequestParam String name) {
//        return chatRoomService.createRoom(name);
//    }
//    // 채팅방 입장 화면
////    @GetMapping("/room/enter/{roomId}")
////    public String roomDetail(Model model, @PathVariable String roomId) {
////        model.addAttribute("roomId", roomId);
////        return "/chat/roomdetail";
////    }
//
//    // 어떤 채팅방에 메시지 보내기
//    // 어떤 채팅방 메시지들 읽기
//    @GetMapping("/room/{roomId}")
//    @ResponseBody
//    public ChatRoom roomInfo(@PathVariable String roomId) {
//        return chatRoomService.findById(roomId);
//    }


    // 채팅방 나가기
}
