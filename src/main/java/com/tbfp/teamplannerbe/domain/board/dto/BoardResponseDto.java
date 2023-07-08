package com.tbfp.teamplannerbe.domain.board.dto;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.boardWithCommentListResponseDto;
import lombok.*;

import java.util.ArrayList;
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
        private List<boardWithCommentListResponseDto> comments=new ArrayList<>();

        public BoardDetailResponseDto(Board board) {
            this.activitiy_name = board.getActivityName();
            this.activity_url = board.getActivityUrl();
            this.activity_img = board.getActivityImg();
            this.activitiy_detail = board.getActivityDetail();
            this.category = board.getCategory();
            this.company_Type = board.getCompanyType();
            this.target = board.getTarget();
            this.activity_area = board.getActivityArea();
            this.recruitment_period = board.getRecruitmentPeriod();
            this.recruitment_count = board.getRecruitmentCount();
            this.meetingTime = board.getMeetingTime();
            this.homepage = board.getHomepage();
            this.activity_benefits = board.getActivityBenefits();
            this.interest_area = board.getInterestArea();
            this.activity_field = board.getActivityField();
            this.prize_scale = board.getPrizeScale();
            this.competition_category = board.getCompetitionCategory();
            this.preferred_skills = board.getPreferredSkills();
            this.activity_period = board.getActivityPeriod();
            this.comments=board.getComments().stream().
                    map(i-> new CommentResponseDto.boardWithCommentListResponseDto(i))
                    .collect(Collectors.toList());
        }

        public static BoardResponseDto.BoardDetailResponseDto toDTO(Board board) {
            return BoardDetailResponseDto.builder()
                    .activitiy_name(board.getActivityName())
                    .activity_url(board.getActivityUrl())
                    .activity_img(board.getActivityImg())
                    .activitiy_detail(board.getActivityDetail())
                    .category(board.getCategory())
                    .company_Type(board.getCompanyType())
                    .target(board.getTarget())
                    .activity_area(board.getActivityArea())
                    .recruitment_period(board.getRecruitmentPeriod())
                    .recruitment_count(board.getRecruitmentCount())
                    .meetingTime(board.getMeetingTime())
                    .homepage(board.getHomepage())
                    .activity_benefits(board.getActivityBenefits())
                    .interest_area(board.getInterestArea())
                    .activity_field(board.getActivityField())
                    .prize_scale(board.getPrizeScale())
                    .competition_category(board.getCompetitionCategory())
                    .preferred_skills(board.getPreferredSkills())
                    .activity_period(board.getActivityPeriod())
                    .comments(board.getComments().stream().map(i -> new CommentResponseDto.boardWithCommentListResponseDto(i)).collect(Collectors.toList()))
                    .build();

        }
    }


    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class BoardSimpleListResponseDto {

        private Long boardId;
        private String activitiyName; // 공고 제목
        private String activityImg; //이미지
        private String category; //카테고리
        private Long view; //조회수
        private Long likeCount; //좋아요



        public BoardSimpleListResponseDto(Board board) {
            this.boardId=board.getId();
            this.activitiyName = board.getActivityName();
            this.activityImg = board.getActivityImg();
            this.category = board.getCategory();
            this.view=board.getView();
            this.likeCount= board.getLikeCount();
        }

        public static BoardResponseDto.BoardSimpleListResponseDto toDTO(Board board) {
            return BoardSimpleListResponseDto.builder()
                    .boardId(board.getId())
                    .activitiyName(board.getActivityName())
                    .activityImg(board.getActivityImg())
                    .category(board.getCategory())
                    .view(board.getView())
                    .likeCount(board.getLikeCount())
                    .build();
        }
    }
}
