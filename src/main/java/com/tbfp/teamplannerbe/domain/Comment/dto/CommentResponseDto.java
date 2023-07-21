package com.tbfp.teamplannerbe.domain.comment.dto;


import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

public class CommentResponseDto {


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreatedCommentResponseDto {
        private String content;
        private Long boardId;
        private String username;
        private LocalDateTime createdDate;
        private Boolean isConfidential;
        private Long commentId;

    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BoardWithCommentListResponseDto {
        private String username;
        private Long parentId;
        private String content;
        private String updatedAt;
        private boolean isConfidential;


        public BoardWithCommentListResponseDto(Comment comment) {
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
    public static class UpdatedCommentResponseDto {

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
    public static class CreatedchildCommentResponseDto {


        private String content;
        private Long boardId;
        private String username;
        private LocalDateTime createdDate;
        private String isConfidential;
        private Long commentId;
        private Long parentId;

    }
}
