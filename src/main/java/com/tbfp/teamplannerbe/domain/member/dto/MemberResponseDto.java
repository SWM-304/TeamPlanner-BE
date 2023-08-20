package com.tbfp.teamplannerbe.domain.member.dto;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto.RecruitmentWithMemberWithApply;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    public static class CheckDuplicateUsernameResponseDto{
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class CheckDuplicateNicknameResponseDto{
        private String message;
    }

    @Builder
    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class EmailResponseDto{
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

    @Builder
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class RecruitmentApplicantResponseDto{
        private String activityName; // 공모전 , 대외활동 명
        @Builder.Default
        private List<RecruitmentWithMemberWithApply> applicantIntro=new ArrayList<>();



        public RecruitmentApplicantResponseDto(Recruitment recruitment) {
            this.activityName = recruitment.getBoard().getActivityName();
            this.applicantIntro = recruitment.getRecruitmentApplyList().stream()
                    .map(i -> new RecruitmentResponseDto.RecruitmentWithMemberWithApply(i)).collect(Collectors.toList());

        }
    }
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class MemberInfoDto{
        private Long memberId;
        private String nickname;
        private String profileImage;

        public static MemberInfoDto toDto(Member member){
            return MemberInfoDto.builder()
                    .memberId(member.getId())
                    .nickname(member.getNickname())
                    .profileImage(member.getBasicProfile().getProfileImage())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class getMemberInfoDto {
        private String username;
        private String nickname;
        private String profileImg;
    }
}