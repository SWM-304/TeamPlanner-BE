package com.tbfp.teamplannerbe.domain.chat.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ChattingRoomCheckResponseDto {
    private boolean roomCheck;


    @Builder
    public ChattingRoomCheckResponseDto(boolean roomCheck) {
        this.roomCheck = roomCheck;
    }
}
