package com.tbfp.teamplannerbe.domain.team.service;

import com.tbfp.teamplannerbe.domain.profile.dto.ProfileRequestDto;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto.CreatTeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto.createdTeamResponseDto;

public interface TeamService {
    createdTeamResponseDto createTeam(String username, CreatTeamRequestDto creatTeamRequestDto);

    void deleteTeamMember(String username,Long teamId,Long memberId);

    void deleteTeam(String username,Long teamId);

    ProfileResponseDto.SubmitEvaluationResponseDto submitEvaluation(TeamRequestDto.SubmitEvaluationRequestDto submitEvaluationRequestDto, Long teamId, Long subjectMemberId, String username);

}
