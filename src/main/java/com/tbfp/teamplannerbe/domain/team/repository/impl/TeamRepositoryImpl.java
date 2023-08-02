package com.tbfp.teamplannerbe.domain.team.repository.impl;

import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.TeamQuerydslRepository;

import java.util.List;

import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;
import static com.tbfp.teamplannerbe.domain.profile.entity.QBasicProfile.basicProfile;
import static com.tbfp.teamplannerbe.domain.team.entity.QMemberTeam.memberTeam;
import static com.tbfp.teamplannerbe.domain.team.entity.QTeam.team;

public class TeamRepositoryImpl extends Querydsl4RepositorySupport implements TeamQuerydslRepository {

    public TeamRepositoryImpl() {
        super(Team.class);
    }

    public List<Team> basicSelect() {
        return select(team)
                .from(team)
                .fetch();
    }
    public List<Team> findAllTeamsByMemberId(Long memberId){
        return select(team)
                .from(team)
                .leftJoin(team.memberTeams, memberTeam)
                .leftJoin(memberTeam.member, member)
                .leftJoin(member.basicProfile, basicProfile)
                .where(member.id.eq(memberId))
                .fetch();
    }
}
