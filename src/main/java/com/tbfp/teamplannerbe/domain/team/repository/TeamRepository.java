package com.tbfp.teamplannerbe.domain.team.repository;

import com.tbfp.teamplannerbe.domain.team.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {
    Team findByRecruitmentId(Long recruitmentId);

    Optional<Team> findByTeamLeader(String username);

    Optional<Team> findById(Long teamId);
}
