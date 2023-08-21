package com.tbfp.teamplannerbe.domain.team.dto;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.CRUDType;
import com.tbfp.teamplannerbe.domain.profile.entity.Evaluation;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

public class TeamRequestDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    @Builder
    public static class CreatTeamRequestDto {

        private List<Long> selectedUserIds;
        @NotNull
        private Long recruitId;
        @NotNull
        private String teamName;
        @NotNull
        private Long maxTeamSize;
        @NotNull
        private String startDate;
        @NotNull
        private String endDate;

    }
}
