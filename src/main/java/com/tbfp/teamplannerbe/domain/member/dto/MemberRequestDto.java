package com.tbfp.teamplannerbe.domain.member.dto;

import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.VerifyPurpose;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.entity.Profile;
import lombok.*;

import javax.validation.constraints.*;
import java.time.LocalDate;

public class MemberRequestDto {


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class MemberLoginRequestDto {
        private String username;
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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpRequestDto {

        @NotEmpty(message = "아이디 설정은 필수입니다.")
        @Size(min = 4, max = 32, message = "아이디를 4~32글자로 설정해주세요.")
        private String username;

        @NotEmpty(message = "비밀번호 설정은 필수입니다.")
        @Size(min = 8, max = 64, message = "비밀번호를 8~64글자의 영문+숫자 조합으로 설정해주세요.")
        @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*\\d)[a-zA-Z\\d]+$", message = "비밀번호는 영문과 숫자의 조합이어야 합니다.")
        private String password;

        @NotEmpty(message = "이메일 입력은 필수입니다.")
        @Pattern(regexp = "^[a-zA-Z0-9+-_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$", message = "이메일 형식이 맞지 않습니다.")
        private String email;

        @Pattern(regexp = "^\\d{2,3}-\\d{3,4}-\\d{4}$", message = "올바른 전화번호를 입력해주세요.")
        private String phone;

//        private Boolean state;

//        private ProviderType providerType;
//
//        private String providerId;

        //선택
        private String profileIntro;

        private String profileImage;

        private Job job;

        private Education education;

        private Integer educationGrade;

        private LocalDate birth;

        private String address;

        private Gender gender;

        private String kakaoId;

        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 맞지 않습니다.")
        private String contactEmail;

        private Long isPublic;

        @NotNull
        @AssertTrue(message = "아이디 중복 검사가 필요합니다.")
        private Boolean usernameChecked;

        @NotNull
        @AssertTrue(message = "이메일 인증이 필요합니다.")
        private Boolean emailChecked;

        public Member toMember(){
            return Member.builder().
                    username(username).
                    password(password).
                    email(email).
                    phone(phone).
                    state(true).
                    memberRole(MemberRole.MEMBER).
                    providerType(null).
                    providerId(null).
                    build();
        }

        public Profile toProfile(Member member){
            return Profile.builder().
                    profileIntro(profileIntro).
                    profileImage(profileImage).
                    job(job).
                    education(education).
                    educationGrade(educationGrade).
                    birth(birth).
                    address(address).
                    gender(gender).
                    kakaoId(kakaoId).
                    contactEmail(contactEmail).
                    isPublic(isPublic).
                    member(member).
                    build();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CheckDuplicateRequestDto {
        private String username;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailAddressRequestDto {
        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 맞지 않습니다.")
        private String emailAddress;
        private VerifyPurpose verifyPurpose;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationRequestDto {

        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 맞지 않습니다.")
        private String emailAddress;
        private String code;
        private VerifyPurpose verifyPurpose;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotUsernameRequestDto {
        private String emailAddress;
        private Boolean emailChecked;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ForgotPasswordRequestDto {
        private String username;
        private String emailAddress;
        private Boolean emailChecked;
    }

}
