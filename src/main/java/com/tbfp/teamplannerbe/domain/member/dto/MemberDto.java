package com.tbfp.teamplannerbe.domain.member.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tbfp.teamplannerbe.domain.member.Education;
import com.tbfp.teamplannerbe.domain.member.Job;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;

public class MemberDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class ProfileInfoForScoringDto {
        //memberid
        private Long id;
        //basicprofile
        @Enumerated(EnumType.STRING)
        private Job job;

        @Enumerated(EnumType.STRING)
        private Education education;

        private LocalDate admissionDate;

        private LocalDate birth;

        private String address;

        //techStack
        //map : techStackItem.id, skillLevel
        private List<TechStackItemDto> techStackItems;

        //activity
        //이름비슷한정도 -> 갯수
        private List<String> activitySubjects;

        //certification
        //이름비슷한정도 -> 갯수
        private List<String> certificationNames;

        //evaluation
        private Double averageStat;

        @QueryProjection
        public ProfileInfoForScoringDto(Long id, Job job, Education education, LocalDate admissionDate, LocalDate birth, String address, List<TechStackItemDto> techStackItems, List<String> activitySubjects, List<String> certificationNames){
            this.id = id;
            this.job = job;
            this.education = education;
            this.admissionDate = admissionDate;
            this.birth = birth;
            this.address = address;
            this.techStackItems = techStackItems;
            this.activitySubjects = activitySubjects;
            this.certificationNames = certificationNames;
            this.averageStat=0.0;
        }
        @Getter
        @Builder
        @NoArgsConstructor(access = AccessLevel.PROTECTED)
        public static class TechStackItemDto {
            private Long id;
            private String name;
            private Integer skillLevel;

            @QueryProjection
            public TechStackItemDto(Long id, String name, Integer skillLevel){
                this.id = id;
                this.name = name;
                this.skillLevel = skillLevel;
            }
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class ScoreAndSimilaritiesDto{
        private Long id;
        private Double score;
        private List<String> similarities;

        @QueryProjection
        public ScoreAndSimilaritiesDto(Long id, Double score, List<String> similarities){
            this.id = id;
            this.score = score;
            this.similarities = similarities;
        }
    }
}

