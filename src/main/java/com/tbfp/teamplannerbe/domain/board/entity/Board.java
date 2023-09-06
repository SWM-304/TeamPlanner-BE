package com.tbfp.teamplannerbe.domain.board.entity;

import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.boardLike.entity.BoardLike;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
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
    @Column(columnDefinition = "MEDIUMTEXT")
    private String homepage; // 주최 url 주소

    private String activityBenefits; // 활동혜택

    private String interestArea; //관심분야

    private String activityField; //활동분야

    private String prizeScale; // 시상규모

    private String competitionCategory; // 공모분야

    private String preferredSkills; //우대역량

    private String activityPeriod; //활동기간

    private Long view;

    private Long likeCount;


    @Builder.Default
    @OneToMany(mappedBy="board")
    private List<Comment> comments=new ArrayList<>();


    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Builder.Default
    @OneToMany(mappedBy = "board")
    private List<Recruitment> recruitment= new ArrayList<>();







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
                .activitiyName(activityName)
                .activityUrl(activityField)
                .activityImg(activityDetail)
                .activitiyDetail(activityArea)
                .category(activityBenefits)
                .companyType(activityImg)
                .target(activityPeriod)
                .activityArea(activityUrl)
                .recruitmentPeriod(category)
                .recruitmentCount(target)
                .meetingTime(companyType)
                .homepage(recruitmentCount)
                .activityBenefits(recruitmentPeriod)
                .interestArea(interestArea)
                .activityField(preferredSkills)
                .prizeScale(prizeScale)
                .competitionCategory(competitionCategory)
                .preferredSkills(meetingTime)
                .activityPeriod(homepage)
                .build();

    }

    public void plusViewCount(Long view){
        this.view=view;
    }
    public void plusLikeCount(Long count) {
        this.likeCount=count;
    }
    public void minusLikeCount(Long count) {
        this.likeCount=count;
    }
}
