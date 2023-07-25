package com.tbfp.teamplannerbe.domain.profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SkillLevel {
    EXPERT("전문가"),
    ADVANCED("상"),
    INTERMEDIATE("중"),
    BASIC("하"),
    BEGINNER("입문");

    private final String label;
}
