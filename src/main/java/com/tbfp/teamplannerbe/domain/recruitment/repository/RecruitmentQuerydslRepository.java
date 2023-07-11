package com.tbfp.teamplannerbe.domain.recruitment.repository;

import com.tbfp.teamplannerbe.domain.recruitment.condition.RecruitmentSearchCondition;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto.RecruitmentSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface RecruitmentQuerydslRepository {
    Page<RecruitmentSearchDto> searchPage(RecruitmentSearchCondition recruitmentSearchCondition, Pageable pageable);
}
