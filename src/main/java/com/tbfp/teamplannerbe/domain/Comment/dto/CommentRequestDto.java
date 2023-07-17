package com.tbfp.teamplannerbe.domain.comment.dto;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class CommentRequestDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CommentSendRequestDto {

        @NotBlank
        private Long boardId;
        @NotBlank
        private String content;
        @NotBlank
        private String memberId;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class bigCommentSendRequestDto {

        @NotBlank
        private Long parentCommentId;
        @NotBlank
        private Long boardId;
        @NotBlank
        private String content;
        @NotBlank
        private String memberId;

        private boolean isConfidential;

        public boolean isConfidential() {
            return isConfidential;
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CommentUpdateRequestDto{
        @NotBlank
        private Long commentId;
        @NotBlank
        private Long boardId;
        @NotBlank
        private String content;


    }

}
