package com.tbfp.teamplannerbe.domain.team.repository;

import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import com.tbfp.teamplannerbe.domain.team.entity.Team;

import java.util.List;

public interface MemberTeamQuerydslRepository {
    List<MemberTeam> basicSelect();

    List<MemberTeam> findDistinctTeamsByMemberId(Long memberId);

    List<Long> findMemberIdsByTeamIds(List<Long> teamIds);
    MemberTeam findByMemberIdsAndTeamIds(Long memberId1, Long memberId2, Long teamId);
}
