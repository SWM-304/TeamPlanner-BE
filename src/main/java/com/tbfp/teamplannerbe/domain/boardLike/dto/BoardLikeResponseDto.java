package com.tbfp.teamplannerbe.domain.boardLike.dto;

import lombok.*;

public class BoardLikeResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class boardStateResponseDto{
        private Boolean stateMessage;
        private Long boardLikeId;
    }

}
