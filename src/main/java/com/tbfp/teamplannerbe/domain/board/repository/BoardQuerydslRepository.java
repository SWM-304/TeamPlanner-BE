package com.tbfp.teamplannerbe.domain.board.repository;

import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardQuerydslRepository {


    Page<Board> getBoardList(BoardSearchCondition condition, Pageable pageable);

    List<Board> getBoardAndComment(Long boardId);

    Page<Board> searchBoardList(String searchWord, Pageable pageable,BoardSearchCondition boardSearchCondition);
}
