package com.tbfp.teamplannerbe.domain.board.dto;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import lombok.*;

public class BoardRequestDto {



    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class createBoardResquestDto {
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

        public Board toEntity(Member member) {
            return Board.builder()
                    .activityName(activitiyName)
                    .activityUrl(activityUrl)
                    .activityImg(activityImg)
                    .activityDetail(activitiyDetail)
                    .category(category)
                    .companyType(companyType)
                    .target(target)
                    .activityArea(activityArea)
                    .recruitmentPeriod(recruitmentPeriod)
                    .recruitmentCount(recruitmentCount)
                    .meetingTime(meetingTime)
                    .homepage(homepage)
                    .activityBenefits(activityBenefits)
                    .interestArea(interestArea)
                    .activityField(activityField)
                    .prizeScale(prizeScale)
                    .competitionCategory(competitionCategory)
                    .preferredSkills(preferredSkills)
                    .activityPeriod(activityPeriod)
                    .likeCount(0L)
                    .view(0L)
                    .member(member)
                    .build();
        }
    }


    @Setter
    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class updateBoardReqeustDto {
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

        public Board toEntity(){
            return Board.builder()
                    .activityName(activitiyName)
                    .activityUrl(activityUrl)
                    .activityImg(activityImg)
                    .activityDetail(activitiyDetail)
                    .category(category)
                    .companyType(companyType)
                    .target(target)
                    .activityArea(activityArea)
                    .recruitmentPeriod(recruitmentPeriod)
                    .recruitmentCount(recruitmentCount)
                    .meetingTime(meetingTime)
                    .homepage(homepage)
                    .activityBenefits(activityBenefits)
                    .interestArea(interestArea)
                    .activityField(activityField)
                    .prizeScale(prizeScale)
                    .competitionCategory(competitionCategory)
                    .preferredSkills(preferredSkills)
                    .activityPeriod(activityBenefits)
                    .build();


        }

    }
}
