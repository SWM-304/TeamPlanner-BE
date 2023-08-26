package com.tbfp.teamplannerbe.domain.recruitmentLike.dto;

import lombok.*;

public class RecruitmentLikeResponseDto {
    @Getter
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @ToString
    public static class RecruitmentLikeSuccessResponseDto{

        private Long recruitmentLikeId;

        @Builder
        public RecruitmentLikeSuccessResponseDto(Long recruitmentLikeId) {
            this.recruitmentLikeId = recruitmentLikeId;
        }
    }
}
