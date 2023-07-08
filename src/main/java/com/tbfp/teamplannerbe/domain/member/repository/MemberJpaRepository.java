package com.tbfp.teamplannerbe.domain.member.repository;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberJpaRepository extends JpaRepository<Member,Long> {
}
