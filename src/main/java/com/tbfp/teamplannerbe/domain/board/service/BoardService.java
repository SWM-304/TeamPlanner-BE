package com.tbfp.teamplannerbe.domain.board.service;


import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface BoardService {


    Long upsert(Board board);

    BoardResponseDto.BoardDetailResponseDto getBoardDetail(Long boardId);

//    List<BoardResponseDto.BoardSimpleListResponseDto> getBoardList(String category);

    Page<BoardResponseDto.BoardSimpleListResponseDto> searchPageSimple(BoardSearchCondition condition, Pageable pageable);
}
