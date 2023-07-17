package com.tbfp.teamplannerbe.domain.recruitment.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitmentComment.entity.RecruitmentComment;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class RecruitmentResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor
    public static class RecruitmentSearchDto {
        private Long id;
        private String title;
        private Integer viewCount;
        private Integer likeCount;
        private LocalDateTime createdAt;

        @QueryProjection
        public RecruitmentSearchDto(Long id, String title, Integer viewCount, Integer likeCount, LocalDateTime createdAt) {
            this.id = id;
            this.title = title;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.createdAt = createdAt;
        }
    }
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitmentCreateResponseDto {
        private Long id;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitmentReadResponseDto {
        private Long id;
        private String title;
        private String content;
        private Integer maxMemberSize;
        private Integer currentMemberSize;
        private Integer viewCount;
        private Integer likeCount;

        public static RecruitmentReadResponseDto toDto(Recruitment recruitment) {
            return builder()
                    .id(recruitment.getId())
                    .title(recruitment.getTitle())
                    .content(recruitment.getContent())
                    .maxMemberSize(recruitment.getMaxMemberSize())
                    .currentMemberSize(recruitment.getCurrentMemberSize())
                    .viewCount(recruitment.getViewCount())
                    .likeCount(recruitment.getLikeCount())
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class RecruitmentWithCommentResponseDto {
        private Long id;
        private String title;
        private String content;
        private Integer maxMemberSize;
        private Integer currentMemberSize;
        private Integer viewCount;
        private Integer likeCount;
        @Builder.Default
        private List<RecruitmentCommentDto> commentList = new ArrayList<>();

        public static RecruitmentWithCommentResponseDto toDto(boolean isAuthorOfRecruitment, String username, Recruitment recruitment) {
            return builder()
                    .id(recruitment.getId())
                    .title(recruitment.getTitle())
                    .content(recruitment.getContent())
                    .maxMemberSize(recruitment.getMaxMemberSize())
                    .currentMemberSize(recruitment.getCurrentMemberSize())
                    .viewCount(recruitment.getViewCount())
                    .likeCount(recruitment.getLikeCount())
                    .commentList(
                            recruitment.getCommentList().stream().map(c -> RecruitmentCommentDto.toDto(isAuthorOfRecruitment, username, c)).collect(Collectors.toList())
                    )
                    .build();
        }

        @Getter
        @Builder
        @NoArgsConstructor
        @AllArgsConstructor
        private static class RecruitmentCommentDto {
            private Long id;
            private String content;
            private LocalDateTime createdAt;
            private Long parentCommentId;
            private String memberUsername;
            private String memberProfileImg;

            public static RecruitmentCommentDto toDto(boolean isAuthorOfRecruitment, String username, RecruitmentComment recruitmentComment) {
                String dtoUsername;
                String dtoContent;

                if (recruitmentComment.isDeleted()) {
                    dtoUsername = "알수없음";
                    dtoContent = "삭제된 댓글입니다";
                } else if (recruitmentComment.isConfidential() && !(
                        isAuthorOfRecruitment || recruitmentComment.getMember().getUsername().equals(username)
                        )) {
                    dtoUsername = "알수없음";
                    dtoContent = "익명 댓글입니다";
                } else {
                    dtoContent = recruitmentComment.getContent();
                    dtoUsername = recruitmentComment.getMember().getUsername();
                }
                return builder()
                        .id(recruitmentComment.getId())
                        .content(dtoContent)
                        .createdAt(recruitmentComment.getCreatedAt())
                        .memberUsername(dtoUsername)
                        .parentCommentId(recruitmentComment.getParentComment() == null ? null : recruitmentComment.getParentComment().getId())
//                        .memberProfileImg(recruitmentComment.getMember().getProfileImg()) // future
                        .build();
            }
        }
    }


}
