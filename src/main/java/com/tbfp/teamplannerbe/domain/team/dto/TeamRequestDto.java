package com.tbfp.teamplannerbe.domain.team.dto;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.CRUDType;
import com.tbfp.teamplannerbe.domain.profile.entity.Evaluation;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.util.List;

public class TeamRequestDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreatTeamRequestDto {

        private List<Long> selectedUserIds;
        private Long recruitId;
        private String teamName;
        private Long maxTeamSize;
        private String startDate;
        private String endDate;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class SubmitEvaluationRequestDto {
        private String comment;

        @Min(value = 1,message = "평가 점수는 1 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 1 이상 10 이하여야 합니다.")
        private Integer stat1;

        @Min(value = 1,message = "평가 점수는 1 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 1 이상 10 이하여야 합니다.")
        private Integer stat2;

        @Min(value = 1,message = "평가 점수는 1 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 1 이상 10 이하여야 합니다.")
        private Integer stat3;

        @Min(value = 1,message = "평가 점수는 1 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 1 이상 10 이하여야 합니다.")
        private Integer stat4;

        @Min(value = 1,message = "평가 점수는 1 이상 10 이하여야 합니다.")
        @Max(value = 10, message = "평가 점수는 1 이상 10 이하여야 합니다.")
        private Integer stat5;

        private CRUDType crudType;

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
}
