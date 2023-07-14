package com.tbfp.teamplannerbe.domain.comment.dto;


import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class CommentResponseDto {


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class boardWithCommentListResponseDto{
        private String username;
        private Long parentId;
        private String content;
        private String updatedAt;
        private boolean isConfidential;


        public boardWithCommentListResponseDto(Comment comment) {
            this.username = comment.getMember().getUsername();
            this.content = comment.getContent();
            this.updatedAt = String.valueOf(comment.getUpdatedAt());
            this.isConfidential=comment.isConfidential();
            this.parentId = comment.getParentComment()==null ? null : comment.getParentComment().getId();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class updatedCommentResponseDto {

        @NotBlank
        private Long boardId;
        @NotBlank
        private String content;
        @NotBlank
        private String memberId;

    }
}
