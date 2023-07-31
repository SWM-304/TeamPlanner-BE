package com.tbfp.teamplannerbe.domain.team.repository.impl;

import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.MemberTeamQuerydslRepository;

import java.util.List;

import static com.tbfp.teamplannerbe.domain.team.entity.QMemberTeam.memberTeam;
import static com.tbfp.teamplannerbe.domain.team.entity.QTeam.team;

public class MemberTeamRepositoryImpl extends Querydsl4RepositorySupport implements MemberTeamQuerydslRepository {

    public MemberTeamRepositoryImpl() {
        super(MemberTeam.class);
    }

    public List<MemberTeam> basicSelect() {
        return select(memberTeam)
                .from(memberTeam)
                .fetch();
    }

    public List<Team> findAllTeamsByMemberId(Long memberId){
        return select(team)
                .from(memberTeam)
                .join(memberTeam.team, team)
                .where(memberTeam.member.id.eq(memberId))
                .fetchJoin()
                .fetch();
    }


}
