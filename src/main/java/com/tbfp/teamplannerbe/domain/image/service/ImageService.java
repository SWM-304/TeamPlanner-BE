package com.tbfp.teamplannerbe.domain.image.service;

import com.tbfp.teamplannerbe.domain.image.dto.ImageResponseDto;

public interface ImageService {
    ImageResponseDto.GetPreSignedUrlResponseDto getPreSignedUrl(String username, String extension);

    void deleteProfileImage(String username, String filename);
}
