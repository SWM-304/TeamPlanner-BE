package com.tbfp.teamplannerbe.domain.member;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum Gender {
    MALE("남성"),
    FEMALE("여성"),
    UNKNOWN("해당 없음");;

    private final String label;
}
