package com.tbfp.teamplannerbe.domain.profile.entity;

import com.tbfp.teamplannerbe.domain.common.base.BaseTimeEntity;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Evaluation extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EVALUATION_ID")
    private Long id;

    private String comment;

    private Integer stat1;

    private Integer stat2;

    private Integer stat3;

    private Integer stat4;

    private Integer stat5;

    @ManyToOne
    @JoinColumn(name = "AUTHOR_MEMBER_ID")
    private Member authorMember;

    @ManyToOne
    @JoinColumn(name = "SUBJECT_MEMBER_ID")
    private Member subjectMember;

    //팀 추가 필요(마구현)
    @OneToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    public ProfileResponseDto.EvaluationResponseDto toDto(){
        return ProfileResponseDto.EvaluationResponseDto.builder()
                .id(id)
                .comment(comment)
                .stat1(stat1)
                .stat2(stat2)
                .stat3(stat3)
                .stat4(stat4)
                .stat5(stat5)
                .build();
    }
}
