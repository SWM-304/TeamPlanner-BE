package com.tbfp.teamplannerbe.domain.board.dto;

import com.querydsl.core.annotations.QueryProjection;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.entity.BoardStateEnum;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.BoardWithCommentListResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.commentToCommentListResponseDto;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
        private Long recruitmentWriterCount; // 모집 글 갯수
        private List<BoardWithCommentListResponseDto> comments=new ArrayList<>();



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
            this.recruitmentWriterCount=board.getRecruitment().stream().count();
            // 현재 null인 값만 가져오고 있음
            if (board.getComments() != null) {
                this.comments = board.getComments().stream()
                        .filter(comment -> comment.getParentComment() == null) // comment 는 부모
                        .map(comment -> {
                            List<Comment> childComments = board.getComments().stream()
                                    .filter(c -> {  // c는 자식으로
                                        Comment parentComment = c.getParentComment();
                                        return parentComment != null && Objects.equals(parentComment.getId(), comment.getId());
                                    })
                                    .collect(Collectors.toList());
                            return new BoardWithCommentListResponseDto(comment, childComments);
                        })
                        .collect(Collectors.toList());
            }


//            if(board.getComments()!=null){
//                this.comments=board.getComments().stream().
//                        map(i-> new BoardWithCommentListResponseDto(i))
//                        .collect(Collectors.toList());
//            }

        }

    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    public static class BoardSimpleListResponseDto {

        private Long boardId;
        private String activitiyName; // 공고 제목
        private String activityImg; //이미지
        private String category; //카테고리
        private Long viewCount; //조회수
        private Long likeCount; //좋아요
        private String recruitmentPeriod;
        private Integer deadlineInDays;
        private Integer commentCount;

        @QueryProjection
        public BoardSimpleListResponseDto(Long boardId, String activitiyName, String activityImg, String category, Long viewCount, Long likeCount, String recruitmentPeriod, Integer deadlineInDays, Integer commentCount) {
            this.boardId = boardId;
            this.activitiyName = activitiyName;
            this.activityImg = activityImg;
            this.category = category;
            this.viewCount = viewCount;
            this.likeCount = likeCount;
            this.recruitmentPeriod = recruitmentPeriod;
            this.deadlineInDays = deadlineInDays;
            this.commentCount = commentCount;
        }

//        public static BoardResponseDto.BoardSimpleListResponseDto toDTO(Board board) {
//
//            String[] day = board.getRecruitmentPeriod().split("~");
//
//            // "yyyy.MM.dd" 형식의 날짜 문자열을 LocalDateTime으로 변환
//            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.M.d");
//            LocalDateTime specificDate = LocalDate.parse(day[1].trim(), formatter).atStartOfDay();
//            // 현재 날짜와 마감일 사이의 날짜 차이 계산
//            Long deadlineInDays = ChronoUnit.DAYS.between(LocalDateTime.now(), specificDate);
//            if (deadlineInDays>=0){
//                return BoardSimpleListResponseDto.builder()
//                        .recruitmentPeriod(board.getRecruitmentPeriod())
//                        .boardId(board.getId())
//                        .deadlineInDays(deadlineInDays)
//                        .activitiyName(board.getActivityName())
//                        .activityImg(board.getActivityImg())
//                        .category(board.getCategory())
//                        .viewCount(board.getView())
//                        .likeCount(board.getLikeCount())
//                        .commentCount(board.getComments().stream().count())
//                        .build();
//            }
//            return null;
//        }
    }

    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class savedBoardIdResponseDto{
        private Long boardId;
    }


    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class boardSearchListResponseDto {

        private Long boardId;
        private String activitiyName; // 공고 제목
        private String activityImg; //이미지
        private String category; //카테고리
        private Long viewCount; //조회수
        private Long likeCount; //좋아요
        private String recruitmentPeriod;
        private Long deadlineInDays;
        private Long commentCount;
        private Long recruitmentCount;
        private String activityField;
        private String companyType;
        private BoardStateEnum boardStateEnum;

        public static BoardResponseDto.boardSearchListResponseDto toDTO(Board board) {
            String[] day = board.getRecruitmentPeriod().split("~");

            // "yyyy.MM.dd" 형식의 날짜 문자열을 LocalDateTime으로 변환
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yy.M.d");
            LocalDateTime specificDate = LocalDate.parse(day[1].trim(), formatter).atStartOfDay();

            // 현재 날짜와 마감일 사이의 날짜 차이 계산
            Long deadlineInDays = ChronoUnit.DAYS.between(LocalDateTime.now(), specificDate);
            if(deadlineInDays>=0){
                return boardSearchListResponseDto.builder()
                        .recruitmentPeriod(board.getRecruitmentPeriod())
                        .companyType(board.getCompanyType())
                        .boardId(board.getId())
                        .activityField(board.getActivityField())
                        .deadlineInDays(deadlineInDays)
                        .recruitmentCount(board.getRecruitment().stream().count())
                        .activitiyName(board.getActivityName())
                        .activityImg(board.getActivityImg())
                        .category(board.getCategory())
                        .viewCount(board.getView())
                        .boardStateEnum(BoardStateEnum.ONGOING)
                        .likeCount(board.getLikeCount())
                        .commentCount(board.getComments().stream().count())
                        .build();
            }
                return boardSearchListResponseDto.builder()
                        .recruitmentPeriod(board.getRecruitmentPeriod())
                        .boardId(board.getId())
                        .companyType(board.getCompanyType())
                        .activityField(board.getActivityField())
                        .deadlineInDays(deadlineInDays)
                        .recruitmentCount(board.getRecruitment().stream().count())
                        .activitiyName(board.getActivityName())
                        .activityImg(board.getActivityImg())
                        .category(board.getCategory())
                        .viewCount(board.getView())
                        .boardStateEnum(BoardStateEnum.CLOSED)
                        .likeCount(board.getLikeCount())
                        .commentCount(board.getComments().stream().count())
                        .build();

        }
    }
}
