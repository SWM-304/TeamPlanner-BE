package com.tbfp.teamplannerbe.domain.recruitment.repository;

import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.Optional;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentQuerydslRepository{

    @Query(
            "select r from Recruitment r left join fetch r.commentList where r.id = :recruitmentId"
    )
    Optional<Recruitment> findByIdFetchComment(@Param("recruitmentId") Long recruitmentId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            "select r from Recruitment r where r.id = :recruitmentId"
    )
    Optional<Recruitment> findByIdForUpdate(@Param("recruitmentId") Long recruitmentId);
}
