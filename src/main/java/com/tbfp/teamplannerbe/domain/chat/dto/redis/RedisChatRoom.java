package com.tbfp.teamplannerbe.domain.chat.dto.redis;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import javax.persistence.Id;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@RedisHash(value = "chatRoom")
public class RedisChatRoom {

    @Id
    private String id;

    @Indexed
    private Integer chatroomNo;

    @Indexed
    private String username;

    @Builder
    public RedisChatRoom(Integer chatroomNo, String username) {
        this.chatroomNo = chatroomNo;
        this.username = username;
    }
}
