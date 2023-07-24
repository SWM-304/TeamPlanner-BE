package com.tbfp.teamplannerbe.domain.recruitmentApply.service;

import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyRequestDto;
import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyResponseDto;

public interface RecruitmentApplyService {
    RecruitmentApplyResponseDto.CreateApplyResponse apply(String username, Long recruitmentId, RecruitmentApplyRequestDto.CreateApplyRequest createApplyRequest);
    void cancelApply(String username, Long recruitmentId);
}
