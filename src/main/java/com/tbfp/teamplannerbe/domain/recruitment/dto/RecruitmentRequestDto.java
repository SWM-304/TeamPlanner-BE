package com.tbfp.teamplannerbe.domain.recruitment.dto;


import com.querydsl.core.annotations.QueryProjection;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import lombok.*;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class RecruitmentRequestDto {



    @Getter
    @Builder
    @NoArgsConstructor
    public static class RecruitmentSearchDto {
        private String title;
        private Integer viewCount;
        private Integer likeCount;
        private LocalDateTime createdAt;

        @QueryProjection
        public RecruitmentSearchDto(String title, Integer viewCount, Integer likeCount, LocalDateTime createdAt) {
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
    public static class RecruitmentCreateRequestDto {

        @NotEmpty
        private String title;
        @NotEmpty
        private String content;

        @Builder.Default
        private Integer currentMemberSize = 0;
        @Builder.Default
        private Integer maxMemberSize = 0;

        @NotNull
        private Long boardId;

        public Recruitment toEntity(Member author, Board board) {

            return Recruitment.builder()
                    .title(title)
                    .content(content)
                    .currentMemberSize(currentMemberSize)
                    .maxMemberSize(maxMemberSize)
                    .author(author)
                    .board(board)
                    .build();
        }
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class RecruitmentUpdateRequestDto {
        private String newTitle;
        private String newContent;
        private Integer newCurrentMemberSize;
        private Integer newMaxMemberSize;
    }
}
