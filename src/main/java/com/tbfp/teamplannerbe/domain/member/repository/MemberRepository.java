package com.tbfp.teamplannerbe.domain.member.repository;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface MemberRepository extends JpaRepository<Member, Long>, MemberQuerydslRepository {
    Optional<Member> findByUsername(String userId);

    Optional<Member> findByNickname(String nickname);

    Optional<Member> findByUsernameAndEmail(String userId,String email);

    Optional<Member> findByNicknameAndEmail(String nickname,String email);
}
