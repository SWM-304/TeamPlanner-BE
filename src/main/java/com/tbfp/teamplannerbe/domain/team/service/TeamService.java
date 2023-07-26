package com.tbfp.teamplannerbe.domain.team.service;

import com.tbfp.teamplannerbe.domain.team.dto.TeamReqeustDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamReqeustDto.CreatTeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto.createdTeamResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto.deleteTeamMemberResponseDto;

import java.text.ParseException;
import java.util.List;

public interface TeamService {
    createdTeamResponseDto createTeam(String username, CreatTeamRequestDto creatTeamRequestDto);

    void deleteTeamMember(String username,Long teamId,Long memberId);

    void deleteTeam(String username,Long teamId);

    List<TeamResponseDto.GetMyTeamResponseDto> getMyTeam(String username);
}
