package com.tbfp.teamplannerbe.domain.team.dto;

import lombok.*;

import java.util.List;

public class TeamReqeustDto {
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
