package com.tbfp.teamplannerbe.domain.member.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

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
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckDuplicateResponseDto{
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailAddressResponseDto{
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class VerificationResponseDto{
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ForgotUsernameResponseDto {
        private List<String> usernames;
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ForgotPasswordResponseDto {
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class ErrorResponseDto{
        private HttpStatus httpStatus;
        private Integer code;
        private String message;
    }

}