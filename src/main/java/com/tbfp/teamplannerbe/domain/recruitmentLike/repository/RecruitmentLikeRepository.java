package com.tbfp.teamplannerbe.domain.recruitmentLike.repository;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitmentLike.entity.RecruitmentLike;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecruitmentLikeRepository extends JpaRepository<RecruitmentLike, Long> {
    Optional<RecruitmentLike> findByMemberAndRecruitment(Member member, Recruitment recruitment);
}
