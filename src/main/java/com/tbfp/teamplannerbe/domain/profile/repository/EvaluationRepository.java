package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.profile.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EvaluationRepository extends JpaRepository<Evaluation,Long> {
    Optional<List<Evaluation>> findAllBySubjectMemberId(Long subjectMemberId);

    Optional<Evaluation> findById(Long id);

    Optional<Evaluation> findByAuthorMemberIdAndSubjectMemberId(Long authorMemberId, Long subjectMemberId);
}
