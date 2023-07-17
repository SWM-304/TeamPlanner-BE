package com.tbfp.teamplannerbe.domain.recruitment.repository;

import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecruitmentRepository extends JpaRepository<Recruitment, Long>, RecruitmentQuerydslRepository{
}
