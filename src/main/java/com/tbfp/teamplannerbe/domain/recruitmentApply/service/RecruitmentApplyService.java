package com.tbfp.teamplannerbe.domain.recruitmentApply.service;

import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyRequestDto;
import com.tbfp.teamplannerbe.domain.recruitmentApply.dto.RecruitmentApplyResponseDto;

import java.util.List;

public interface RecruitmentApplyService {
    RecruitmentApplyResponseDto.CreateApplyResponse apply(String username, Long recruitmentId, RecruitmentApplyRequestDto.CreateApplyRequest createApplyRequest);
    void cancelApply(String username, Long recruitmentId);

    List<RecruitmentApplyResponseDto.GetApplyResponse> getApplyList(String username);
}
