package com.tbfp.teamplannerbe.domain.boardLike.service.impl;


import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.boardLike.dto.BoardLikeResponseDto;
import com.tbfp.teamplannerbe.domain.boardLike.entity.BoardLike;
import com.tbfp.teamplannerbe.domain.boardLike.repository.BoardLikeRepository;
import com.tbfp.teamplannerbe.domain.boardLike.service.BoardLikeService;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardLikeServiceImpl implements BoardLikeService {

    private final MemberRepository memberRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    /**
     *  공고 좋아요테이블에 좋아요내역 추가 및 공고테이블 좋아요카운트 동기화
     *  주의 동시성 발생할 가능성 있음
     *
     * @param boardId
     * @param username
     *
     * @return void
     */
    @Override
    @Transactional
    public void createLikesOnBoard(Long boardId, String username) {

        Member member = memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));

        Optional<BoardLike> boardLike = boardLikeRepository.findByBoardIdAndMemberId(boardId, member.getId());

        if(boardLike.isPresent()){
            throw new ApplicationException(ALREADY_LIKED);
        }

        // boardTable의 board 동기화
        board.plusLikeCount(board.getLikeCount()+1);

        //boardLike 테이블에 좋아요 정보최신화
        BoardLike boardlike=BoardLike.builder()
                .member(member)
                .board(board)
                .build();
        boardLikeRepository.save(boardlike);

    }

    /**
     * 좋아요 내역을 삭제
     * @param boardLikeId
     * @param username
     */
    @Override
    @Transactional
    public void deleteLikesOnBoard(Long boardLikeId, String username) {
        memberRepository.findByUsername(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        BoardLike boardLike = boardLikeRepository.findById(boardLikeId).orElseThrow(
                () -> new ApplicationException(NOT_FOUND));


        //좋아요 삭제 시 해당 boardLikeCount -=1
        boardLike.getBoard().minusLikeCount(boardLike.getBoard().getLikeCount()-1);

        boardLikeRepository.deleteById(boardLikeId);
        
    }

    /**
     * 좋아요 상태조회
     * @param boardId
     * @param username
     */
    @Override
    public BoardLikeResponseDto.boardStateResponseDto getLikeState(Long boardId,String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        Optional<BoardLike> board = boardLikeRepository.findByBoardIdAndMemberId(boardId, member.getId());

        BoardLikeResponseDto.boardStateResponseDto responseDto = board
                .map(b -> BoardLikeResponseDto.boardStateResponseDto.builder()
                        .stateMessage(true)
                        .boardLikeId(b.getId())
                        .build())
                .orElse(BoardLikeResponseDto.boardStateResponseDto.builder()
                        .stateMessage(false)
                        .build());

        return responseDto;

    }
}
