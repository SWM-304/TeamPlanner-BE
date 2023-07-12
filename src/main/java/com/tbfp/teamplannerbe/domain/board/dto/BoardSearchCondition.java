package com.tbfp.teamplannerbe.domain.board.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardSearchCondition {

    private String category;
}
