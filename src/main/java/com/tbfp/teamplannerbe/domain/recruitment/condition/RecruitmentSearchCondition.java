package com.tbfp.teamplannerbe.domain.recruitment.condition;


import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RecruitmentSearchCondition {
    private String titleContain;
    private String contentContain;
    private String authorNameContain;
    private Long boardIdContain;
    private String boardTitleContain;
}
