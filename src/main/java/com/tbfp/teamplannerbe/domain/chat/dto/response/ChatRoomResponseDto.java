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
        private Integer readCount;

        @Builder
        public ChatRoomListDto(Long roomId, List<Map<String, String>> memberList, String lastMessageText, String lastMessageTime,Integer readCount) {
            this.roomId = roomId;
            this.memberList = memberList;
            this.lastMessageText = lastMessageText;
            this.lastMessageTime = lastMessageTime;
            this.readCount=readCount;
        }
    }
}
