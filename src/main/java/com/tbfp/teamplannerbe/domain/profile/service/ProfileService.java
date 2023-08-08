package com.tbfp.teamplannerbe.domain.profile.service;

import com.tbfp.teamplannerbe.domain.profile.dto.ProfileRequestDto;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamRequestDto;
import com.tbfp.teamplannerbe.domain.team.dto.TeamResponseDto;

import java.util.List;
import com.tbfp.teamplannerbe.domain.profile.entity.BasicProfile;

public interface ProfileService {
    ProfileResponseDto.ShowProfileResponseDto showProfile(String nickname);

    ProfileResponseDto.GetProfileResponseDto getProfile(String username);

    ProfileResponseDto.CreateProfileResponseDto createProfile(ProfileRequestDto.CreateProfileRequestDto createProfileRequestDto, String username);

    ProfileResponseDto.UpdateProfileResponseDto updateProfile(ProfileRequestDto.UpdateProfileRequestDto updateProfileRequestDto, String username);

    void deleteProfile(String username);

    ProfileResponseDto.CreateEvaluationResponseDto createEvaluation(ProfileRequestDto.CreateEvaluationRequestDto createEvaluationRequestDto, Long teamId, Long subjectMemberId, String username);

    ProfileResponseDto.UpdateEvaluationResponseDto updateEvaluation(ProfileRequestDto.UpdateEvaluationRequestDto updateEvaluationRequestDto, Long teamId, Long subjectMemberId, String username);

    void deleteEvaluation(ProfileRequestDto.DeleteEvaluationRequestDto deleteEvaluationRequestDto, Long teamId, Long subjectMemberId, String username);

    List<ProfileResponseDto.EvaluationResponseDto> getAllEvaluations(String username);
    
    BasicProfile getBasicProfile(String username);
}
