package com.tbfp.teamplannerbe.domain.chat.entity;

import lombok.*;

import javax.persistence.Id;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChatMessage implements Serializable {

    @Id
    private String id;

    public enum MessageType {
        ENTER, TALK
    }
    private MessageType type;
    //채팅방 ID
    private Long roomId;
    //보내는 사람
    private Long senderId;
    //내용
    private String message;

    private LocalDateTime createdAt;

}
