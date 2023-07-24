package com.tbfp.teamplannerbe.domain.recruitmentComment.repository;

import com.tbfp.teamplannerbe.domain.recruitmentComment.entity.RecruitmentComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface RecruitmentCommentRepository extends JpaRepository<RecruitmentComment, Long> {
    @Query("select rc from RecruitmentComment rc join fetch rc.member where rc.id = :commentId")
    Optional<RecruitmentComment> findByIdFetchMember(@Param("commentId") Long commentId);
}
