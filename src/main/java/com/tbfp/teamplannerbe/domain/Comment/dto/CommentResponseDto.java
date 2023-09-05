package com.tbfp.teamplannerbe.domain.comment.dto;


import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommentResponseDto {


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class CreatedCommentResponseDto {
        private String content;
        private Long boardId;
        private String nickname;
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
        private Long commentId;
        private Integer commentCount;
        private String nickName;
        private String profileImage;
        private Boolean state;
        private String nickname;
        @Builder.Default
        List<commentToCommentListResponseDto> childCommentList=new ArrayList<>();



        public BoardWithCommentListResponseDto(Comment comment, List<Comment> childComments) {
            this.username = comment.getMember().getUsername();
            this.content = comment.getContent();
            this.updatedAt = String.valueOf(comment.getUpdatedAt());
            this.isConfidential=comment.isConfidential();
            this.parentId = comment.getParentComment()==null ? null : comment.getParentComment().getId();
            this.commentId=comment.getId();
            this.commentCount=comment.getChildCommentCount();
            this.nickName=comment.getMember().getNickname();
            this.profileImage=comment.getMember().getBasicProfile()==null ? null : comment.getMember().getBasicProfile().getProfileImage();
            this.state=comment.isState();
            this.nickname=comment.getMember().getNickname();
            if (childComments != null) {
                this.childCommentList = childComments.stream()
                        .map(BoardWithCommentListResponseDto::mapToCommentToCommentListResponseDto)
                        .collect(Collectors.toList());
            }
        }

        private static commentToCommentListResponseDto mapToCommentToCommentListResponseDto(Comment comment) {
            return new commentToCommentListResponseDto(comment);
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

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class commentToCommentListResponseDto {

        private String username;
        private String content;
        private LocalDateTime updatedAt;
        private boolean isConfidential;
        private Long commentId;
        private Long parentId;
        private String nickName;
        private String profileImage;

        public commentToCommentListResponseDto(Comment comment) {
            this.username = comment.getMember().getUsername();
            this.content = comment.getContent();
            this.updatedAt = comment.getUpdatedAt();
            this.isConfidential = comment.isConfidential();
            this.commentId = comment.getId();
            this.parentId = comment.getParentComment()==null ? null : comment.getParentComment().getId();
            this.nickName=comment.getMember().getNickname();
            this.profileImage=comment.getMember().getBasicProfile()==null ? null : comment.getMember().getBasicProfile().getProfileImage();
        }
    }
}
