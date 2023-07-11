package com.tbfp.teamplannerbe.domain.board.service.impl;

import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto.createBoardResquestDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardRequestDto.updateBoardReqeustDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto.BoardDetailResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto.savedBoardIdResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.board.service.BoardService;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberJpaRepository;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final MemberJpaRepository memberJpaRepository;


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
            findBoard.plusViewCount(0L);
            findBoard.plusLikeCount(0L);
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

    @Transactional
    public List<BoardDetailResponseDto> getBoardDetail(Long boardId) {
        Board findBoard = boardRepository.findById(boardId).
                orElseThrow(()-> new ApplicationException(ApplicationErrorType.BOARD_NOT_FOUND));

        findBoard.plusViewCount(findBoard.getView()+1);

        boardRepository.save(findBoard);

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

    @Override
    public savedBoardIdResponseDto createBoard(createBoardResquestDto createBoardResquestDto,String userId) {


        Member member = memberJpaRepository.findByLoginId(userId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));

        Board entityBoard = boardRepository.save(createBoardResquestDto.toEntity(member));
        Board save = boardRepository.save(entityBoard);


        return savedBoardIdResponseDto
                .builder()
                .boardId(save.getId())
                .build();
    }

    @Override
    public void deleteBoard(Long boardId, String userId) {

        memberJpaRepository.findByLoginId(userId)
                        .orElseThrow(()->new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(ApplicationErrorType.BOARD_NOT_FOUND));

        boardRepository.delete(board);
    }

    @Override
    public Boolean updateBoard(updateBoardReqeustDto updateBoardReqeustDto, String userId) {

        Board findBoard = boardRepository.findById(updateBoardReqeustDto.getBoardId()).
                orElseThrow(()->new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));
        memberJpaRepository.findByLoginId(userId)
                .orElseThrow(()->new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));

        findBoard.overwrite(updateBoardReqeustDto.toEntity());

        return Boolean.TRUE;
    }


}




