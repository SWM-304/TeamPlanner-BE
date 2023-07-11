package com.tbfp.teamplannerbe.domain.board.service;


import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto.createBoardResquestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto.updateBoardReqeustDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto.savedBoardIdResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.security.Principal;
import java.util.List;

public interface BoardService {


    Long upsert(Board board);

    List<BoardResponseDto.BoardDetailResponseDto> getBoardDetail(Long boardId);

//    List<BoardResponseDto.BoardSimpleListResponseDto> getBoardList(String category);

    Page<Board> searchPageSimple(BoardSearchCondition condition, Pageable pageable);


    savedBoardIdResponseDto createBoard(createBoardResquestDto createBoardResquestDto, String userId);

    void deleteBoard(Long boardId, String userId);

    Boolean updateBoard(updateBoardReqeustDto updateBoardReqeustDto, String userId);
}
