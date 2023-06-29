package com.tbfp.teamplannerbe.domain.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Education {
    MIDDLE_SCHOOL("중학교재학"),
    MIDDLE_SCHOOL_GRADUATE("중학교졸업"),
    HIGH_SCHOOL("고등학교재학"),
    HIGH_SCHOOL_GRADUATE("고등학교졸업"),
    COLLEGE("대학교재학"),
    COLLEGE_GRADUATE("대학교졸업"),
    GRADUATE_SCHOOL("대학원재학"),
    GRADUATE_SCHOOL_GRADUATE("대학원졸업");

    private final String displayName;

}