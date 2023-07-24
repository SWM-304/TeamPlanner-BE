package com.tbfp.teamplannerbe.domain.recruitmentComment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class RecruitmentCommentRequestDto {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateRequestDto {
        private Long recruitmentId;
        private Boolean isConfidential;
        private String content;
    }
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class ReadRequestDto {
//
//    }
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UpdateRequestDto {
        private  String newContent;
    }
//    @Getter
//    @NoArgsConstructor
//    @AllArgsConstructor
//    @Builder
//    public static class DeleteRequestDto {
//
//    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class CreateCoCommentRequestDto {
        private Long recruitmentId;
        private Boolean isConfidential;
        private String content;
    }
}
