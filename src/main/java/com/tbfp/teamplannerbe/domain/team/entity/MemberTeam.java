package com.tbfp.teamplannerbe.domain.team.entity;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitmentApply.entity.RecruitmentApplyStateEnum;
import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MemberTeam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name="MEMBER_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Member member;

    @Enumerated(EnumType.STRING)
    private MemberTeamStateEnum role;

    @JoinColumn(name="TEAM_ID")
    @ManyToOne(fetch = FetchType.LAZY)
    private Team team;


}
