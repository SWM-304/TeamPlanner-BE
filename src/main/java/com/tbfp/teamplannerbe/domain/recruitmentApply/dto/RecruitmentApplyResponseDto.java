package com.tbfp.teamplannerbe.domain.recruitmentApply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecruitmentApplyResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateApplyResponse {
        private Long recruitmentApplyId;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteApplyResponse {
        private Long recruitmentApplyId;
    }
}
