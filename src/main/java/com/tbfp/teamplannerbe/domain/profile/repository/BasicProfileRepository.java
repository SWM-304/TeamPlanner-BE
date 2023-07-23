package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.entity.BasicProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BasicProfileRepository extends JpaRepository<BasicProfile,Long>, BasicProfileQuerydslRepository {
    Optional<BasicProfile> findByMemberId(Long memberId);

    Optional<BasicProfile> findByProfileImageAndMemberId(String profileImage, Long memberId);

    void deleteByMember(Member member);
}
