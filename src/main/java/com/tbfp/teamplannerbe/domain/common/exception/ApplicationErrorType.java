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

    USER_NOT_FOUND(HttpStatus.BAD_REQUEST, -2, "유저를 찾을 수 없습니다."),

    UNSUCCESSFUL_AUTHENTICATION(HttpStatus.BAD_REQUEST, -3, "로그인 실패"),

    INVALID_REFRESH_TOKEN(HttpStatus.BAD_REQUEST, -4, "유효하지 않은 리프레시 토큰"),

    BOARD_NOT_FOUND(HttpStatus.NO_CONTENT,-5,"공고글을 찾을 수 없습니다"),

    COMMNET_NOT_FINE(HttpStatus.NO_CONTENT,-6,"댓글을 찾을 수 없습니다"),

    REFRESH_TOKEN_FOR_USER_NOT_FOUND(HttpStatus.NO_CONTENT, -7, "리프레시 토큰을 찾을 수 없습니다."),

    MEMBER_REGISTER_FAIL(HttpStatus.BAD_REQUEST, -20000, "회원 생성에 실패했습니다."),

    DUPLICATE_USERNAME(HttpStatus.CONFLICT,-20001,"이미 사용중인 아이디입니다."),

    UNVERIFIED_EMAIL(HttpStatus.BAD_REQUEST,-20002,"이메일 인증이 완료되지 않았습니다."),

    INVALID_CONTACT_EMAIL(HttpStatus.BAD_REQUEST, -20003, "입력하신 아이디에 등록된 이메일이 아닙니다."),

    MAIL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, -20004, "메일 전송 중 오류가 발생했습니다."),

    DUPLICATE_NICKNAME(HttpStatus.BAD_REQUEST,-20005,"이미 사용중인 닉네임입니다."),

    SIGNUP_REQUEST_INVALID_USERNAME(HttpStatus.BAD_REQUEST, -21000, "username"),

    SIGNUP_REQUEST_INVALID_PASSWORD(HttpStatus.BAD_REQUEST, -21001, "password"),

    SIGNUP_REQUEST_INVALID_EMAIL(HttpStatus.BAD_REQUEST, -21002, "email"),

    SIGNUP_REQUEST_INVALID_PHONE(HttpStatus.BAD_REQUEST, -21003, "phone"),

    SIGNUP_REQUEST_INVALID_CONTACTEMAIL(HttpStatus.BAD_REQUEST, -21004, "contactEmail"),

    VERIFICATION_CODE_UNMATCHED(HttpStatus.UNAUTHORIZED,-22000, "인증번호가 일치하지 않습니다."),

    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST,-22001, "인증번호가 만료되어 재전송했습니다."),

    VERIFICATION_CODE_UNPROVIDED(HttpStatus.BAD_REQUEST,-22002, "인증번호가 발급되지 않아 재전송했습니다."),

    ALREADY_LIKE_BOARD(HttpStatus.BAD_REQUEST,-8,"이미 좋아요를 누르셨습니다"),

    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST,-9,"댓글을 찾을 수 없습니다");


    private HttpStatus httpStatus;
    private Integer code;
    private String message;
}