package com.tbfp.teamplannerbe.domain.common.exception;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ApplicationErrorType {


    NOT_FOUND(HttpStatus.NOT_FOUND, -100, "요청한 페이지를 찾을 수 없습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, -101, "권한이 없습니다."),
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

    SIGNUP_REQUEST_INVALID_NICKNAME(HttpStatus.BAD_REQUEST, -21005, "nickname"),


    VERIFICATION_CODE_UNMATCHED(HttpStatus.UNAUTHORIZED,-22000, "인증번호가 일치하지 않습니다."),

    VERIFICATION_CODE_EXPIRED(HttpStatus.BAD_REQUEST,-22001, "인증번호가 만료되어 재전송했습니다."),

    VERIFICATION_CODE_UNPROVIDED(HttpStatus.BAD_REQUEST,-22002, "인증번호가 발급되지 않아 재전송했습니다."),


    PROFILE_NOT_EXIST(HttpStatus.BAD_REQUEST, -23000,"프로필이 존재하지 않습니다."),

    PROFILE_ALREADY_EXIST(HttpStatus.BAD_REQUEST,-23001,"프로필이 이미 존재하여 중복 생성이 불가합니다."),

    IMAGE_DELETION_UNAUTHORIZED(HttpStatus.BAD_REQUEST,-23002,"선택한 이미지 명을 삭제할 권한이 없습니다"),

    IMAGE_DOESNT_EXIST(HttpStatus.BAD_REQUEST,-23003,"이미지가 존재하지 않습니다."),

    PROFILE_DELETION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR,-23004,"프로필 삭제중 문제가 발생했습니다."),

    USER_NOT_IN_TEAM(HttpStatus.BAD_REQUEST,-24000,"사용자가 소속된 팀이 아닙니다."),

    EVALUATION_SCORE_NOT_IN_SCOPE(HttpStatus.BAD_REQUEST,-24001,"평가 점수는 총합 0이상 30이하여야 합니다."),

    ALREADY_LIKE_BOARD(HttpStatus.BAD_REQUEST,-8,"이미 좋아요를 누르셨습니다"),

    COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST,-9,"댓글을 찾을 수 없습니다"),

    ALREADY_LIKED(HttpStatus.BAD_REQUEST, -30000, "이미 좋아요 한 글입니다."),
    RECRUITMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, -44001, "모집글을 찾을 수 없습니다."),
    RECRUITMENT_COMMENT_NOT_FOUND(HttpStatus.BAD_REQUEST, -44002, "모집글 댓글을 찾을 수 없습니다."),
    RECRUITMENT_APPLY_ALREADY_APPLIED(HttpStatus.BAD_REQUEST, -50001, "이미 참여 신청한 모집글입니다."),
    RECRUITMENT_APPLY_NOT_APPLIED(HttpStatus.BAD_REQUEST, -50002, "참여 신청하지 않은 모집글입니다."),
    TEAM_CAPACITY_EXCEEDED(HttpStatus.BAD_REQUEST, -62000, "팀 최대인원 수를 초과하였습니다."),
    ALREADY_TEAM_ACCEPT(HttpStatus.BAD_REQUEST,-62001,"이미 승인된 사람이 포함되어 있습니다"),
    AUTHOR_CANNOT_PARTICIPATE(HttpStatus.BAD_REQUEST,-62002,"작성자는 참여신청을 할 수 없습니다"),
    TEAM_NOT_FOUND(HttpStatus.BAD_REQUEST,-62003,"팀을 찾을 수 없습니다");





    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
