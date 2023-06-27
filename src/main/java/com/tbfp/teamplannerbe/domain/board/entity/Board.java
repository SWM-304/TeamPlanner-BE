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


    private String activity_key;

    private String activitiy_name; // 공고 제목

    private String activity_url; //주최 url

    private String activity_img; //이미지
    @Column(columnDefinition = "TEXT")
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




    public void overwrite(Board board) {
        this.id = board.id;
        this.activitiy_name = board.activitiy_name;
        this.activity_url = board.activity_url;
        this.activity_img = board.activity_img;
        this.activitiy_detail =board.activitiy_detail;
        this.category = board.category;
        this.company_Type = board.company_Type;
        this.target = board.target;
        this.activity_area = board.activity_area;
        this.recruitment_period = board.recruitment_period;
        this.recruitment_count = board.recruitment_count;
        this.meetingTime = board.meetingTime;
        this.homepage = board.homepage;
        this.activity_benefits = board.activity_benefits;
        this.interest_area = board.interest_area;
        this.activity_field = board.activity_field;
        this.prize_scale = board.prize_scale;
        this.competition_category = board.competition_category;
        this.preferred_skills = board.preferred_skills;
        this.activity_period = board.activity_period;
        this.activity_key=board.getActivity_key();
    }

    public BoardResponseDto.BoardDetailResponseDto toDTO() {
        return BoardResponseDto.BoardDetailResponseDto.builder()
                .activitiy_name(activitiy_name)
                .activity_field(activity_field)
                .activitiy_detail(activitiy_detail)
                .activity_area(activity_area)
                .activity_benefits(activity_benefits)
                .activity_img(activity_img)
                .activity_period(activity_period)
                .activity_url(activity_url)
                .category(category)
                .target(target)
                .company_Type(company_Type)
                .recruitment_count(recruitment_count)
                .recruitment_period(recruitment_period)
                .interest_area(interest_area)
                .preferred_skills(preferred_skills)
                .prize_scale(prize_scale)
                .competition_category(competition_category)
                .meetingTime(meetingTime)
                .homepage(homepage)
                .build();

    }
}
