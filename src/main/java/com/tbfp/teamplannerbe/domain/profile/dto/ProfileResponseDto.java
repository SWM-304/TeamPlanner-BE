package com.tbfp.teamplannerbe.domain.profile.dto;

import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.CRUDType;
import com.tbfp.teamplannerbe.domain.profile.SkillLevel;
import com.tbfp.teamplannerbe.domain.profile.entity.*;
import lombok.*;

import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

public class ProfileResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BasicProfileResponseDto{
        private Long id;
        private String profileIntro;
        private String profileImage;
        private Job job;
        private Education education;
        private int educationGrade;
        private LocalDate birth;
        private String address;
        private Gender gender;
        private String kakaoId;

        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 맞지 않습니다.")
        private String contactEmail;
        private Long isPublic;
        private Boolean evaluationPublic;
        public BasicProfile toEntity() {
            return BasicProfile.builder()
                    .id(id)
                    .profileIntro(profileIntro)
                    .profileImage(profileImage)
                    .job(job)
                    .education(education)
                    .educationGrade(educationGrade)
                    .birth(birth)
                    .address(address)
                    .gender(gender)
                    .kakaoId(kakaoId)
                    .contactEmail(contactEmail)
                    .isPublic(isPublic)
                    .evaluationPublic(evaluationPublic)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TechStackResponseDto{
        private Long id;
        private Integer experiencedYear;
        private Integer experiencedMonth;
        private SkillLevel skillLevel;
        private TechStackItem techStackItem;

        public TechStack toEntity(Member member){
            return TechStack.builder()
                    .id(id)
                    .experiencedYear(experiencedYear)
                    .experiencedMonth(experiencedMonth)
                    .skillLevel(skillLevel)
                    .techStackItem(techStackItem)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ActivityResponseDto{
        private Long id;
        private String subject;
        private String detail;
        private LocalDate startDate;
        private LocalDate endDate;

        public Activity toEntity(Member member){
            return Activity.builder()
                    .id(id)
                    .subject(subject)
                    .detail(detail)
                    .startDate(startDate)
                    .endDate(endDate)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CertificationResponseDto{
        private Long id;
        private String name;
        private double score;
        private LocalDate gainDate;

        public Certification toEntity(Member member){
            return Certification.builder()
                    .id(id)
                    .name(name)
                    .score(score)
                    .gainDate(gainDate)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class EvaluationResponseDto {
        private Long id;
        private String comment;
        private Integer stat1;
        private Integer stat2;
        private Integer stat3;
        private Integer stat4;
        private Integer stat5;
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ShowProfileResponseDto{
        private BasicProfileResponseDto basicProfile;
        private List<TechStackResponseDto> techStacks;
        private List<ActivityResponseDto> activities;
        private List<CertificationResponseDto> certifications;
        private List<EvaluationResponseDto> evaluations;
        private Boolean evaluationPublic;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GetProfileResponseDto {
        private BasicProfileResponseDto basicProfile;
        private List<TechStackResponseDto> techStacks;
        private List<ActivityResponseDto> activities;
        private List<CertificationResponseDto> certifications;
        private List<EvaluationResponseDto> evaluations;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateProfileResponseDto {
        String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UpdateProfileResponseDto {
        String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class DeleteProfileResponseDto {
        String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateEvaluationResponseDto {
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UpdateEvaluationResponseDto {
        private String message;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class DeleteEvaluationResponseDto {
        private String message;
    }
}
