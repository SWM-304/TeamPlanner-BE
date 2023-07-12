package com.tbfp.teamplannerbe.domain.board.dto;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.boardWithCommentListResponseDto;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BoardResponseDto {

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class BoardDetailResponseDto{

        private String activitiyName; // 공고 제목
        private String activityUrl; //주최 url
        private String activityImg; //이미지
        private String activitiyDetail; // 상세내용
        private String category; //카테고리
        private String companyType; //기업형태
        private String target; // 참여대상
        private String activityArea; //활동지역
        private String recruitmentPeriod; // 접수기간
        private String recruitmentCount; //모집인원
        private String meetingTime; // 모임시간
        private String homepage; // 주최 url 주소
        private String activityBenefits; // 활동혜택
        private String interestArea; //관심분야
        private String activityField; //활동분야
        private String prizeScale; // 시상규모
        private String competitionCategory; // 공모분야
        private String preferredSkills; //우대역량
        private String activityPeriod; //활동기간
        private Long viewCount;
        private Long likeCount;
        private List<boardWithCommentListResponseDto> comments=new ArrayList<>();

        public BoardDetailResponseDto(Board board) {
            this.activitiyName = board.getActivityName();
            this.activityUrl = board.getActivityUrl();
            this.activityImg = board.getActivityImg();
            this.activitiyDetail = board.getActivityDetail();
            this.category = board.getCategory();
            this.companyType = board.getCompanyType();
            this.target = board.getTarget();
            this.activityArea = board.getActivityArea();
            this.recruitmentPeriod = board.getRecruitmentPeriod();
            this.recruitmentCount = board.getRecruitmentCount();
            this.meetingTime = board.getMeetingTime();
            this.homepage = board.getHomepage();
            this.activityBenefits = board.getActivityBenefits();
            this.interestArea = board.getInterestArea();
            this.activityField = board.getActivityField();
            this.prizeScale = board.getPrizeScale();
            this.competitionCategory = board.getCompetitionCategory();
            this.preferredSkills = board.getPreferredSkills();
            this.activityPeriod = board.getActivityPeriod();
            this.viewCount=board.getView();
            this.likeCount=board.getLikeCount();
            if(board.getComments()!=null){
                this.comments=board.getComments().stream().
                        map(i-> new CommentResponseDto.boardWithCommentListResponseDto(i))
                        .collect(Collectors.toList());
            }

        }

        public static BoardResponseDto.BoardDetailResponseDto toDTO(Board board) {
            return BoardDetailResponseDto.builder()
                    .activitiyName(board.getActivityName())
                    .activityUrl(board.getActivityUrl())
                    .activityImg(board.getActivityImg())
                    .activitiyDetail(board.getActivityDetail())
                    .category(board.getCategory())
                    .companyType(board.getCompanyType())
                    .target(board.getTarget())
                    .activityArea(board.getActivityArea())
                    .recruitmentPeriod(board.getRecruitmentPeriod())
                    .recruitmentCount(board.getRecruitmentCount())
                    .meetingTime(board.getMeetingTime())
                    .homepage(board.getHomepage())
                    .activityBenefits(board.getActivityBenefits())
                    .interestArea(board.getInterestArea())
                    .activityField(board.getActivityField())
                    .prizeScale(board.getPrizeScale())
                    .competitionCategory(board.getCompetitionCategory())
                    .preferredSkills(board.getPreferredSkills())
                    .activityPeriod(board.getActivityPeriod())
                    .comments(board.getComments().stream().map(i -> new CommentResponseDto.boardWithCommentListResponseDto(i)).collect(Collectors.toList()))
                    .build();

        }
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
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

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class savedBoardIdResponseDto{
        private Long boardId;
    }
}
