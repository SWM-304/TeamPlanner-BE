package com.tbfp.teamplannerbe.domain.team.repository.impl;

import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.team.entity.Team;
import com.tbfp.teamplannerbe.domain.team.repository.TeamQuerydslRepository;

import java.util.List;

import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;
import static com.tbfp.teamplannerbe.domain.profile.entity.QBasicProfile.basicProfile;
import static com.tbfp.teamplannerbe.domain.recruitment.entity.QRecruitment.recruitment;
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
        List<Long> teamIds = select(memberTeam.team.id)
                .from(memberTeam)
                .where(memberTeam.member.id.eq(memberId))
                .distinct()
                .fetch();

        return select(team)
                .from(team)
                .distinct()
                .leftJoin(team.memberTeams, memberTeam).fetchJoin()
                .leftJoin(memberTeam.member, member).fetchJoin()
                .leftJoin(member.basicProfile,basicProfile).fetchJoin()
                .leftJoin(team.recruitment,recruitment).fetchJoin()
                .leftJoin(recruitment.board,board).fetchJoin()
                .where(team.id.in(teamIds))
                .fetch();
    }
}
