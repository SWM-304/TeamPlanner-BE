package com.tbfp.teamplannerbe.domain.team.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class CommonResponseDto<T> {
    private int code; // 1(성공),-1(실패)
    private String message;
    private T data;
}
