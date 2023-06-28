package com.tbfp.teamplannerbe.domain.Comment.dto;

import com.nimbusds.oauth2.sdk.auth.Secret;
import com.tbfp.teamplannerbe.domain.Comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class CommentRequestDto {

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
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
    @Setter
    @Builder
    @NoArgsConstructor
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

        private boolean Secret;

        public boolean isSecret() {
            return Secret;
        }
    }
    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
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
