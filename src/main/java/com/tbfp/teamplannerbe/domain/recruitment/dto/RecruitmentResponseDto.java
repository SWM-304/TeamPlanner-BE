package com.tbfp.teamplannerbe.domain.recruitment.dto;

import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecruitmentResponseDto {


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

}
