package com.tbfp.teamplannerbe.domain.board.service;


import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;

public interface BoardService {


    Long upsert(Board board);

    BoardResponseDto.BoardDetailResponseDto getBoardDetail(Long boardId);
}
