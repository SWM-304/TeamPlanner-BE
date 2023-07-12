package com.tbfp.teamplannerbe.domain.auth.entity;

import lombok.*;
import org.springframework.data.redis.core.RedisHash;

import javax.persistence.Id;

@RedisHash("refreshToken")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class RefreshToken {
    @Id
    private String id; // member username
    private String token;
}
