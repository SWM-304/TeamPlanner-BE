package com.tbfp.teamplannerbe.domain.member.dto;

import com.tbfp.teamplannerbe.domain.member.ErrorCode;
import lombok.*;

import java.util.List;

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

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class SignUpResponseDto {
        private boolean success;
        private List<String> messages;
        private List<ErrorCode> errorCodes;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailAddressResponseDto{
        private boolean success;
        private List<String> messages;
        private List<ErrorCode> errorCodes;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ForgotUsernameResponseDto {
        private boolean success;
        private List<String> usernames;
        private List<String> messages;
        private List<ErrorCode> errorCodes;
    }

}
