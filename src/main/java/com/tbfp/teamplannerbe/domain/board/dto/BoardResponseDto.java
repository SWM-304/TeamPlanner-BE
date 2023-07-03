package com.tbfp.teamplannerbe.domain.board.dto;

import com.tbfp.teamplannerbe.domain.Comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import lombok.*;

import java.util.List;
import java.util.stream.Collectors;

public class BoardResponseDto {

    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardDetailResponseDto{

        private String activitiy_name; // 공고 제목

        private String activity_url; //주최 url

        private String activity_img; //이미지

        private String activitiy_detail; // 상세내용

        private String category; //카테고리

        private String company_Type; //기업형태

        private String target; // 참여대상

        private String activity_area; //활동지역

        private String recruitment_period; // 접수기간

        private String recruitment_count; //모집인원

        private String meetingTime; // 모임시간

        private String homepage; // 주최 url 주소

        private String activity_benefits; // 활동혜택

        private String interest_area; //관심분야

        private String activity_field; //활동분야

        private String prize_scale; // 시상규모

        private String competition_category; // 공모분야

        private String preferred_skills; //우대역량

        private String activity_period; //활동기간
    }


    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardSimpleListResponseDto {

        private String activitiyName; // 공고 제목

        private String activityImg; //이미지

        private String category; //카테고리

        List<CommentResponseDto.boardWithCommentListResponseDto> comments; // 댓글정보 List 형태로 가져오기



        public BoardSimpleListResponseDto(Board board) {
            this.activitiyName = board.getActivityName();
            this.activityImg = board.getActivityImg();
            this.category = board.getCategory();
            this.comments=board.getComments().stream().
                    map(i-> new CommentResponseDto.boardWithCommentListResponseDto(i))
                    .collect(Collectors.toList());
        }
    }
}
