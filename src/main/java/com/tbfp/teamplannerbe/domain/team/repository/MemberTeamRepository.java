package com.tbfp.teamplannerbe.domain.team.repository;

import com.tbfp.teamplannerbe.domain.team.entity.MemberTeam;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

public interface MemberTeamRepository extends JpaRepository<MemberTeam,Long>, MemberTeamQuerydslRepository {

    Optional<MemberTeam> deleteByTeamIdAndMemberId(Long teamId, Long memberId);

    Optional<MemberTeam> deleteByMemberId(Long memberId);

    List<MemberTeam> findAllByTeamId(Long teamId);

    List<MemberTeam> findAllByMemberId(Long memberId);
}
