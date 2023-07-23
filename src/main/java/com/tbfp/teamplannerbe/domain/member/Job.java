package com.tbfp.teamplannerbe.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {
    MIDDLE_SCHOOL("중학생"),
    HIGH_SCHOOL("고등학생"),
    COLLEGE("대학생"),
    BUSINESS("직장인"),
    SELF_EMPLOYED("자영업자"),
    ENTREPRENUER("사업가"),
    NONE("무직");
    private final String label;

}
