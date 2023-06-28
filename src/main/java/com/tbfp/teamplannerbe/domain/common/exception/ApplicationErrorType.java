package com.tbfp.teamplannerbe.domain.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplicationErrorType {


    GUEST_USER(HttpStatus.UNAUTHORIZED, -1, "소셜로그인유저 추가 회원가입 필요"),

    USER_NOT_FOUND(HttpStatus.NO_CONTENT, -2, "유저를 찾을 수 없습니다."),

    UNSUCCESSFUL_AUTHENTICATION(HttpStatus.BAD_REQUEST, -3, "로그인 실패"),

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, -4, "유효하지 않은 리프레시 토큰"),

    BOARD_NOT_FIND(HttpStatus.NO_CONTENT,-5,"공고글을 찾을 수 없습니다"),

    COMMNET_NOT_FINE(HttpStatus.NO_CONTENT,-6,"댓글을 찾을 수 없습니다");

    private HttpStatus httpStatus;
    private Integer code;
    private String message;
}
