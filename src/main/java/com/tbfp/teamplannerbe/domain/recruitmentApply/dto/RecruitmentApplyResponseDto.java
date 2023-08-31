package com.tbfp.teamplannerbe.domain.recruitmentApply.dto;

import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApply;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecruitmentApplyResponseDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateApplyResponse {
        private Long recruitmentApplyId;
    }


    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class DeleteApplyResponse {
        private Long recruitmentApplyId;
    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class GetApplyResponse {
        private Long recruitmentApplyId;
        private Long boardId;
        private String boardName;
        private Long recruitmentId;
        private String recruitmentTitle;
        private String recruitmentContent;
        private String recruitmentApplyContent;
        private String boardImage;

        public static GetApplyResponse toDto(RecruitmentApply ra) {
            return GetApplyResponse.builder()
                    .boardImage(ra.getRecruitment().getBoard().getActivityImg())
                    .recruitmentApplyId(ra.getId())
                    .boardId(ra.getRecruitment().getBoard().getId())
                    .boardName(ra.getRecruitment().getBoard().getActivityName())
                    .recruitmentId(ra.getRecruitment().getId())
                    .recruitmentTitle(ra.getRecruitment().getTitle())
                    .recruitmentContent(ra.getRecruitment().getContent())
                    .recruitmentApplyContent(ra.getContent())
                    .build();
        }
    }
}
