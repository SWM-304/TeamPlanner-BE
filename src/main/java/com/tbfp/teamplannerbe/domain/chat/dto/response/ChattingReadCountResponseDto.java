package com.tbfp.teamplannerbe.domain.chat.dto.response;


import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class ChattingReadCountResponseDto {
    private Integer readCount;

    @Builder
    public ChattingReadCountResponseDto(Integer readCount) {
        this.readCount = readCount;
    }
}
