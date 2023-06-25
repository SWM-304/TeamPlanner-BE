package com.tbfp.teamplannerbe.domain.member.dto;

import lombok.*;

public class MemberRequestDto {


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginRequestDto {
        private String loginId;
        private String password;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberRenewAccessTokenRequestDto {
        private String refreshToken;
    }

}
