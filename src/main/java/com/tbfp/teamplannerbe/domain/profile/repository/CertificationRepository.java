package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.entity.Certification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CertificationRepository extends JpaRepository<Certification,Long> {
    Optional<List<Certification>> findAllByMemberId(Long memberId);

    void deleteAllByIdInAndMember(List<Long> certificationIds, Member member);

    void deleteAllByMember(Member member);
}