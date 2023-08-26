package com.tbfp.teamplannerbe.domain.recruitment.entity;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentRequestDto.RecruitmentUpdateRequestDto;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApply;
import com.tbfp.teamplannerbe.domain.recruitmentComment.entity.RecruitmentComment;
import com.tbfp.teamplannerbe.domain.recruitmentLike.entity.RecruitmentLike;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Recruitment extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECRUITMENT_ID")
    private Long id;

    private String title;
    private String content;

    @Builder.Default
    private Integer maxMemberSize = 0;
    @Builder.Default
    private Integer currentMemberSize = 0;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @OneToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member author;

    @Builder.Default
    private Integer viewCount = 0;
    @Builder.Default
    private Integer likeCount = 0;

    @OneToMany(mappedBy = "recruitment", fetch = FetchType.LAZY)
    @Builder.Default
    private List<RecruitmentComment> commentList = new ArrayList<>();

    @OneToMany(mappedBy = "recruitment")
    @Builder.Default
    private List<RecruitmentApply> recruitmentApplyList=new ArrayList<>();

    @OneToMany(mappedBy = "recruitment")
    @Builder.Default
    private List<RecruitmentLike> recruitmentLikeList=new ArrayList<>();

    @OneToMany(mappedBy = "recruitment")
    @Builder.Default
    private List<Team> teams=new ArrayList<>();




    public void incrementViewCount() {
        this.viewCount++;
    }


    public void update(RecruitmentUpdateRequestDto recruitmentUpdateRequestDto) {
        this.title = recruitmentUpdateRequestDto.getNewTitle();
        this.content = recruitmentUpdateRequestDto.getNewContent();
        this.currentMemberSize = recruitmentUpdateRequestDto.getNewCurrentMemberSize();
        this.maxMemberSize = recruitmentUpdateRequestDto.getNewMaxMemberSize();
    }

    public void incrementLikeCount() {
        this.likeCount++;
    }

    public void decrementLikeCount() {
        this.likeCount--;
    }
}
