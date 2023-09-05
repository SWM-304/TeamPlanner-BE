package com.tbfp.teamplannerbe.domain.member;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Education {
    COLLEGE("대학교 재학"),
    COLLEGE_GRADUATE("대학교 졸업"),
    GRADUATE_SCHOOL("대학원 재학"),
    GRADUATE_SCHOOL_GRADUATE("대학원 졸업"),
    HIGH_SCHOOL("고등학교 재학"),
    HIGH_SCHOOL_GRADUATE("고등학교 졸업"),
    MIDDLE_SCHOOL("중학교 재학"),
    MIDDLE_SCHOOL_GRADUATE("중학교 졸업"),
    UNKNOWN("해당 없음");

    private final String label;
    @JsonValue
    public String getLabel(){
        return label;
    }

}