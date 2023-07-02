package com.tbfp.teamplannerbe.domain.Comment.dto;

import com.tbfp.teamplannerbe.domain.Comment.entity.Comment;
import lombok.*;

import javax.validation.constraints.NotBlank;

public class CommentResponseDto {


    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class boardWithCommentListResponseDto{
        private String loginId;
        private Long parentId;
        private String content;
        private String updatedAt;
        private boolean isConfidential;


        /**
         *
         * loginId 지금 얘 때문에 공고리스트 출력할 때 쿼리가 3방나감 comment에 userId 컬럼달아 놓으면
         * 쿼리 1방 줄일 수 있음.
         */
        public boardWithCommentListResponseDto(Comment comment) {
            this.loginId = comment.getMember().getLoginId();
            this.content = comment.getContent();
            this.updatedAt = String.valueOf(comment.getUpdatedAt());
            this.isConfidential=comment.isConfidential();
            this.parentId=comment.getParentComment().getId();
        }
    }

    @Getter
    @Setter
    @Builder
    @NoArgsConstructor
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
