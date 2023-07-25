package com.tbfp.teamplannerbe.domain.profile;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TechCategory {
    PLANNING_IDEA("기획/아이디어"),
    ADVERTISING_MARKETING("광고/마케팅"),
    LITERATURE_SCREENPLAY("문학/시나리오"),
    DESIGN("디자인"),
    VIDEO_CONTENTS("영상/콘텐츠"),
    IT_SW("IT/SW"),
    STARTUP("창업/스타트업"),
    FINANCE_ECONOMICS("금융/경제"),
    OTHER("기타");

    private final String label;
}
