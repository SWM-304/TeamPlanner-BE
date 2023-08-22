package com.tbfp.teamplannerbe.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Job {
    COLLEGE("대학생"),
    GRADUATE_SCHOOL("대학원생"),
    JOB_PREPARE("취업준비생"),
    BUSINESS("직장인"),
    HIGH_SCHOOL("고등학생"),
    MIDDLE_SCHOOL("중학생"),
    SELF_EMPLOYED("자영업자"),
    ENTREPRENEUR("사업가"),
    NONE("무직"),
    UNKNOWN("해당 없음");;
    private final String label;

}
