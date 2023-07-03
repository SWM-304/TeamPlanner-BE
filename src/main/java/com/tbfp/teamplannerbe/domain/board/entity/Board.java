package com.tbfp.teamplannerbe.domain.board.entity;


import com.tbfp.teamplannerbe.domain.Comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BOARD_ID")
    private Long id;


    private String activityKey;

    private String activityName; // 공고 제목
    @Column(columnDefinition = "TEXT")
    private String activityUrl; //주최 url

    private String activityImg; //이미지
    @Column(columnDefinition = "MEDIUMTEXT")
    private String activityDetail; // 상세내용

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

    @OneToMany(mappedBy="board",cascade = CascadeType.ALL)
    private List<Comment> comments=new ArrayList<>();

    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;


    public void overwrite(Board board) {
        this.activityName = board.activityName;
        this.activityUrl = board.activityUrl;
        this.activityImg = board.activityImg;
        this.activityDetail =board.activityDetail;
        this.category = board.category;
        this.companyType = board.companyType;
        this.target = board.target;
        this.activityArea = board.activityArea;
        this.recruitmentPeriod = board.recruitmentPeriod;
        this.recruitmentCount = board.recruitmentCount;
        this.meetingTime = board.meetingTime;
        this.homepage = board.homepage;
        this.activityBenefits = board.activityBenefits;
        this.interestArea = board.interestArea;
        this.activityField = board.activityField;
        this.prizeScale = board.prizeScale;
        this.competitionCategory = board.competitionCategory;
        this.preferredSkills = board.preferredSkills;
        this.activityPeriod = board.activityPeriod;
        this.activityKey=board.activityKey;
    }

    public BoardResponseDto.BoardDetailResponseDto toDTO() {
        return BoardResponseDto.BoardDetailResponseDto.builder()
                .activitiy_name(activityName)
                .activity_field(activityField)
                .activitiy_detail(activityDetail)
                .activity_area(activityArea)
                .activity_benefits(activityBenefits)
                .activity_img(activityImg)
                .activity_period(activityPeriod)
                .activity_url(activityUrl)
                .category(category)
                .target(target)
                .company_Type(companyType)
                .recruitment_count(recruitmentCount)
                .recruitment_period(recruitmentPeriod)
                .interest_area(interestArea)
                .preferred_skills(preferredSkills)
                .prize_scale(prizeScale)
                .competition_category(competitionCategory)
                .meetingTime(meetingTime)
                .homepage(homepage)
                .build();

    }
}
