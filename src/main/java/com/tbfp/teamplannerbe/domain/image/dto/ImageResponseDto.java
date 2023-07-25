package com.tbfp.teamplannerbe.domain.image.dto;

import lombok.*;

public class ImageResponseDto {
    @Getter
    @Builder
    @NoArgsConstructor(access = AccessLevel.PROTECTED)
    @AllArgsConstructor
    public static class GetPreSignedUrlResponseDto{
        String preSignedUrl;
    }
}
