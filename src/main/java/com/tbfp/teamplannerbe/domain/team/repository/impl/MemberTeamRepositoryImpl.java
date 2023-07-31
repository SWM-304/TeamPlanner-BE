package com.tbfp.teamplannerbe.domain.team.repository.impl;

import com.querydsl.jpa.JPAExpressions;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.member.entity.QMember;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.MemberTeamQuerydslRepository;

import java.util.List;

import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;
import static com.tbfp.teamplannerbe.domain.team.entity.QMemberTeam.memberTeam;
import static com.tbfp.teamplannerbe.domain.team.entity.QTeam.team;

public class MemberTeamRepositoryImpl extends Querydsl4RepositorySupport implements MemberTeamQuerydslRepository{
    public MemberTeamRepositoryImpl() {
        super(MemberTeam.class);
    }

    public List<MemberTeam> basicSelect() {
        return select(memberTeam)
                .from(memberTeam)
                .fetch();
    }

    public List<MemberTeam> findDistinctTeamsByMemberId(Long memberId){
        return select(memberTeam)
                .from(memberTeam)
                .leftJoin(memberTeam.team,team).fetchJoin()
                .leftJoin(memberTeam.member,member).fetchJoin()
                .where(memberTeam.member.id.eq(memberId))
                .fetch();
    }

    public MemberTeam findByMemberIdsAndTeamIds(Long memberId1, Long memberId2, Long teamId) {
        return select(memberTeam)
                .from(memberTeam)
                .where(memberTeam.member.id.eq(memberId1),
                        memberTeam.team.in(
                                select(memberTeam.team)
                                        .from(memberTeam)
                                        .where(memberTeam.member.id.eq(memberId2))
                        )
                )
                .where(memberTeam.team.id.eq(teamId))
                .fetchOne();
    }
}
