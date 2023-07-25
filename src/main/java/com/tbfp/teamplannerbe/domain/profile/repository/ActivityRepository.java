package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.entity.Activity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityRepository extends JpaRepository<Activity,Long> {
    Optional<List<Activity>> findAllByMemberId(Long memberId);

    void deleteAllByIdInAndMember(List<Long> activityIds, Member member);

    void deleteAllByMember(Member member);
}
