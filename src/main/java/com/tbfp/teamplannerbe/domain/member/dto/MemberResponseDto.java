package com.tbfp.teamplannerbe.domain.member.dto;

import lombok.*;

public class MemberResponseDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginResponseDto {
        private String accessToken;
        private String refreshToken;
    }
    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class MemberRenewAccessTokenResponseDto {
        private String accessToken;
    }

}
