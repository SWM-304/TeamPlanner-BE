package com.tbfp.teamplannerbe.domain.chat.dto.response;

import com.tbfp.teamplannerbe.domain.chat.entity.ChatRoomMember;
import lombok.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ChatRoomResponseDto {

    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChatRoomListDto {
        private Long roomId;
        private List<Map<String, String>> memberList; // List<Map>으로 변경
        private String lastMessageText;
        private String lastMessageTime;

//        public static ChatRoomListDto toDto(ChatRoomMember chatRoomMember) {
//            return builder()
//                    .roomId(chatRoomMember.getChatRoom().getId())
//                    .memberList(chatRoomMember.getChatRoom().getChatRoomMemberList().stream().map(crm -> {return crm.getMember().getNickname();}).collect(Collectors.toList()))
//                    .build();
//        }
//
        @Builder
        public ChatRoomListDto(Long roomId, List<Map<String, String>> memberList, String lastMessageText, String lastMessageTime) {
            this.roomId = roomId;
            this.memberList = memberList;
            this.lastMessageText = lastMessageText;
            this.lastMessageTime = lastMessageTime;
        }
    }
}
