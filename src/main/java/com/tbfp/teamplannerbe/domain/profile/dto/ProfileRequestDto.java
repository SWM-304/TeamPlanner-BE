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
        private int educationGrade;
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
                    .educationGrade(educationGrade)
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
    public static class EvaluationRequestDto {
        private Long id;
        private String comment;
        private Integer stat1;
        private Integer stat2;
        private Integer stat3;
        private Integer stat4;
        private Integer stat5;
        //작성자 정보 필요 없음(익명의 평가로 보이기 때문에)
        //팀 정보를 보여줄 건지는 생각 필요
        private CRUDType crudType;
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
}
