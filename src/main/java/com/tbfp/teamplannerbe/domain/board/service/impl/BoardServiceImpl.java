package com.tbfp.teamplannerbe.domain.board.service.impl;

import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto.BoardDetailResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;


    /**
     *
     * @param board
     * 크롤링 데이터 없으면 save 있으면 overWrite
     * @return findBoard.getId()
     */
    @Transactional
    public Long upsert(Board board) {
        // Check if the board already exists
        Board findBoard = boardRepository.findByActivityKey(board.getActivityKey())
                .orElse(new Board());
        findBoard.overwrite(board);
        if (findBoard.getId() == null) {
            log.info("\tinsert board");
            boardRepository.save(findBoard);
        } else {
            log.info("\toverWrite board");

        }
        return findBoard.getId();
    }

    /**
     *
     * 공모전 대외활동에 대한 상세정보조회
     */

    @Transactional(readOnly = true)
    public List<BoardDetailResponseDto> getBoardDetail(Long boardId) {
        List<Board> board = boardRepository.getBoardAndComment(boardId);

        List<BoardDetailResponseDto> result = board.stream().map(i -> new BoardDetailResponseDto(i))
                .collect(Collectors.toList());
        return result;
    }


//    @Transactional(readOnly = true)
//    public List<BoardResponseDto.BoardSimpleListResponseDto> getBoardList(String category) {
//        List<Board> getBoardList = boardRepository.getBoardListbyCategory(category);
//
//        List<BoardResponseDto.BoardSimpleListResponseDto> boardList = getBoardList.stream().
//                map(i -> new BoardResponseDto.BoardSimpleListResponseDto(i)).
//                collect(Collectors.toList());
//
//        return boardList;
//
//    }

    /**
     *
     * 공모전 대외활동 category 별 List 페이지
     * 동적쿼리를 통한 pagination
     */

    @Override
    @Transactional(readOnly = true)
    public Page<Board> searchPageSimple(BoardSearchCondition condition, Pageable pageable) {
        Page<Board> getBoardList = boardRepository.getBoardList(condition, pageable);

        return getBoardList;
    }


}




