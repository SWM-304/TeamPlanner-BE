package com.tbfp.teamplannerbe.domain.board.dto;


import com.tbfp.teamplannerbe.domain.board.entity.BoardStateEnum;
import lombok.*;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class BoardSearchCondition {

    private String category;
    private String activityField;
    private List<BoardStateEnum>  boardState;
    private String boardRecruitment;
}
