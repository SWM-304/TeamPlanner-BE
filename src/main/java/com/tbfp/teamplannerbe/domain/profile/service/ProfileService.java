package com.tbfp.teamplannerbe.domain.profile.service;

import com.tbfp.teamplannerbe.domain.profile.dto.ProfileRequestDto;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.profile.entity.BasicProfile;

public interface ProfileService {
    ProfileResponseDto.ShowProfileResponseDto showProfile(String nickname);

    ProfileResponseDto.GetProfileResponseDto getProfile(String username);

    ProfileResponseDto.CreateProfileResponseDto createProfile(ProfileRequestDto.CreateProfileRequestDto createProfileRequestDto, String username);

    ProfileResponseDto.UpdateProfileResponseDto updateProfile(ProfileRequestDto.UpdateProfileRequestDto updateProfileRequestDto, String username);

    void deleteProfile(String username);

    BasicProfile getBasicProfile(String username);
}
