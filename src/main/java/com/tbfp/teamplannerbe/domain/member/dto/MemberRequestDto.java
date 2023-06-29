package com.tbfp.teamplannerbe.domain.member.dto;

import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.entity.Profile;
import lombok.*;

import java.time.LocalDate;

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

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SignUpRequestDto {

        private String loginId;

        private String password;

        private String email;

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

        private int educationGrade;

        private LocalDate birth;

        private String address;

        private Gender gender;

        private String kakaoId;

        private String contactEmail;

        private Long isPublic;

        public Member toMember(){
            return Member.builder().
                    loginId(loginId).
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
        private String loginId;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmailAddressDto {
        private String emailAddress;
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VerificationRequestDto {
        private String emailAddress;
        private String code;
    }

}
