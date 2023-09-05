package com.tbfp.teamplannerbe.domain.chat.dto.request;

import com.tbfp.teamplannerbe.domain.chat.entity.ChatMessage;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

public class ChatRoomRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateRoomDto {
        private String targetNickname;
    }

    @Getter
    @Builder
    @Setter
    @AllArgsConstructor(access = AccessLevel.PROTECTED)
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ChattingReqeust {
        private Long chattingRoomId;
        private Long senderId;
        private String content;
        private String createdDate;
        private String createdTime;

        public ChatMessage toChatting() {
            return ChatMessage.builder()
                    .id(UUID.randomUUID().toString())
                    .senderId(senderId)
                    .message(content)
                    .roomId(chattingRoomId)
                    .createdAt(LocalDateTime.now())
                    .build();
        }
    }

}
