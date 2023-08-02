package com.tbfp.teamplannerbe.domain.team.repository;

import com.tbfp.teamplannerbe.domain.team.entity.Team;

import java.util.List;

public interface TeamQuerydslRepository {
    List<Team> basicSelect();
    List<Team> findAllTeamsByMemberId(Long memberId);
}
