package com.tbfp.teamplannerbe.domain.comment.dto;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class CommentRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class createCommentRequestDto {

        @NotBlank
        private Long boardId;
        @NotBlank
        private String content;
        @NotBlank
        private String memberId;
        @NotBlank
        private Boolean isConfidential;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class commentToCommentCreateRequestDto {

        @NotBlank
        private Long parentCommentId;
        @NotBlank
        private Long boardId;
        @NotBlank
        private String content;
        @NotBlank
        private Boolean isConfidential;
    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class updateCommentRequestDto {
        @NotBlank
        private Long commentId;
        @NotBlank
        private Long boardId;
        @NotBlank
        private String content;


    }

}
