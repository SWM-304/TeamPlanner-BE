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
}
