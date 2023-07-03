package com.tbfp.teamplannerbe.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum VerifyPurpose {
    SIGN_UP("회원가입"),
    FORGOT_ID("아이디 찾기"),
    FORGOT_PASSWORD("비밀번호 찾기");

    private final String displayName;
}
