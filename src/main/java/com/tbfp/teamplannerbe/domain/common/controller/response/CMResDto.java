package com.tbfp.teamplannerbe.domain.common.controller.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CMResDto<T>{
    private int code; // 1(성공),-1(실패)
    private String message;
    private T data;
}