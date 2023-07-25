package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.entity.TechStack;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TechStackRepository extends JpaRepository<TechStack,Long> {
    Optional<List<TechStack>> findAllByMemberId(Long memberId);

    void deleteAllByIdInAndMember(List<Long> techStackIds, Member member);

    void deleteAllByMember(Member member);
}