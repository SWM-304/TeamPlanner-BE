package com.tbfp.teamplannerbe.domain.board.entity;


import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class Board extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="BOARD_ID")
    private Long id;


    private String activity_Key;

    private String activitiy_Name; // 공고 제목
    @Column(columnDefinition = "TEXT")
    private String activity_Url; //주최 url

    private String activity_Img; //이미지
    @Column(columnDefinition = "MEDIUMTEXT")
    private String activitiy_Detail; // 상세내용

    private String category; //카테고리

    private String company_Type; //기업형태

    private String target; // 참여대상

    private String activity_Area; //활동지역

    private String recruitment_Period; // 접수기간

    private String recruitment_Count; //모집인원

    private String meeting_Time; // 모임시간

    private String homepage; // 주최 url 주소

    private String activity_Benefits; // 활동혜택

    private String interest_Area; //관심분야

    private String activity_Field; //활동분야

    private String prize_Scale; // 시상규모

    private String competition_Category; // 공모분야

    private String preferred_Skills; //우대역량

    private String activity_Period; //활동기간




    public void overwrite(Board board) {
        this.id = board.id;
        this.activitiy_Name = board.activitiy_Name;
        this.activity_Url = board.activity_Url;
        this.activity_Img = board.activity_Img;
        this.activitiy_Detail =board.activitiy_Detail;
        this.category = board.category;
        this.company_Type = board.company_Type;
        this.target = board.target;
        this.activity_Area = board.activity_Area;
        this.recruitment_Period = board.recruitment_Period;
        this.recruitment_Count = board.recruitment_Count;
        this.meeting_Time = board.meeting_Time;
        this.homepage = board.homepage;
        this.activity_Benefits = board.activity_Benefits;
        this.interest_Area = board.interest_Area;
        this.activity_Field = board.activity_Field;
        this.prize_Scale = board.prize_Scale;
        this.competition_Category = board.competition_Category;
        this.preferred_Skills = board.preferred_Skills;
        this.activity_Period = board.activity_Period;
        this.activity_Key=board.getActivity_Key();
    }

    public BoardResponseDto.BoardDetailResponseDto toDTO() {
        return BoardResponseDto.BoardDetailResponseDto.builder()
                .activitiy_name(activitiy_Name)
                .activity_field(activity_Field)
                .activitiy_detail(activitiy_Detail)
                .activity_area(activity_Area)
                .activity_benefits(activity_Benefits)
                .activity_img(activity_Img)
                .activity_period(activity_Period)
                .activity_url(activity_Url)
                .category(category)
                .target(target)
                .company_Type(company_Type)
                .recruitment_count(recruitment_Count)
                .recruitment_period(recruitment_Period)
                .interest_area(interest_Area)
                .preferred_skills(preferred_Skills)
                .prize_scale(prize_Scale)
                .competition_category(competition_Category)
                .meetingTime(meeting_Time)
                .homepage(homepage)
                .build();

    }
}
