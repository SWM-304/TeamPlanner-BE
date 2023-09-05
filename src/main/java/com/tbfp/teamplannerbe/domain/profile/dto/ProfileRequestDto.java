package com.tbfp.teamplannerbe.domain.profile.dto;

import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Gender;
import com.tbfp.teamplannerbe.domain.member.Job;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.CRUDType;
import com.tbfp.teamplannerbe.domain.profile.SkillLevel;
import com.tbfp.teamplannerbe.domain.profile.entity.*;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

public class ProfileRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BasicProfileRequestDto{
        private Long id;
        private String profileIntro;
        private String profileImage;
        private Job job;
        private Education education;
        private LocalDate admissionDate;
        private LocalDate graduationDate;
        private LocalDate birth;
        private String address;
        private Gender gender;
        private String kakaoId;

        @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+.[A-Za-z]{2,6}$", message = "이메일 형식이 맞지 않습니다.")
        private String contactEmail;
        private Long isPublic;
        private Boolean evaluationPublic;
        private CRUDType crudType;
        public BasicProfile toEntity(Member member) {
            return BasicProfile.builder()
                    .id(id)
                    .profileIntro(profileIntro)
                    .profileImage(profileImage)
                    .job(job)
                    .education(education)
                    .admissionDate(admissionDate)
                    .graduationDate(graduationDate)
                    .birth(birth)
                    .address(address)
                    .gender(gender)
                    .kakaoId(kakaoId)
                    .contactEmail(contactEmail)
                    .isPublic(isPublic)
                    .evaluationPublic(evaluationPublic)
                    .member(member)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class TechStackRequestDto{
        private Long id;
        private Integer experiencedYear;
        private Integer experiencedMonth;
        private SkillLevel skillLevel;
        private TechStackItem techStackItem;
        private CRUDType crudType;

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
    public static class ActivityRequestDto{
        private Long id;
        private String subject;
        private String detail;
        private LocalDate startDate;
        private LocalDate endDate;
        private CRUDType crudType;

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
    public static class CertificationRequestDto{
        private Long id;
        private String name;
        private double score;
        private LocalDate gainDate;
        private CRUDType crudType;

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
    public static class CreateProfileRequestDto{
        private BasicProfileRequestDto basicProfile;
        private List<TechStackRequestDto> techStacks;
        private List<ActivityRequestDto> activities;
        private List<CertificationRequestDto> certifications;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UpdateProfileRequestDto{
        private BasicProfileRequestDto basicProfile;
        private List<TechStackRequestDto> techStacks;
        private List<ActivityRequestDto> activities;
        private List<CertificationRequestDto> certifications;
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreateEvaluationRequestDto {
        private String comment;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat1;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat2;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat3;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat4;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat5;


        public Evaluation toEntity(Member authorMember, Member subjectMember, Team team){
            return Evaluation.builder()
                    .comment(comment)
                    .stat1(stat1)
                    .stat2(stat2)
                    .stat3(stat3)
                    .stat4(stat4)
                    .stat5(stat5)
                    .authorMember(authorMember)
                    .subjectMember(subjectMember)
                    .team(team)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class UpdateEvaluationRequestDto {
        private Long id;

        private String comment;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat1;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat2;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat3;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat4;

        @Min(value = 0,message = "평가 점수는 0 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 0 이상 10 이하여야 합니다.")
        private Integer stat5;

        public Evaluation toEntity(Member authorMember, Member subjectMember, Team team){
            return Evaluation.builder()
                    .id(id)
                    .comment(comment)
                    .stat1(stat1)
                    .stat2(stat2)
                    .stat3(stat3)
                    .stat4(stat4)
                    .stat5(stat5)
                    .authorMember(authorMember)
                    .subjectMember(subjectMember)
                    .team(team)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class DeleteEvaluationRequestDto {
        private Long id;
    }
}
