package com.tbfp.teamplannerbe.domain.team.repository;

import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;

import java.util.List;

public interface MemberTeamQuerydslRepository {
    List<MemberTeam> basicSelect();

    List<Team> findAllTeamsByMemberId(Long memberId);
}
