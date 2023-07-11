package com.tbfp.teamplannerbe.domain.boardLike.service.impl;


import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.boardLike.entity.BoardLike;
import com.tbfp.teamplannerbe.domain.boardLike.repository.BoardLikeRepository;
import com.tbfp.teamplannerbe.domain.boardLike.service.BoardLikeService;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;

@Service
@RequiredArgsConstructor
public class BoardLikeServiceImpl implements BoardLikeService {

    private final MemberJpaRepository memberJpaRepository;
    private final BoardRepository boardRepository;
    private final BoardLikeRepository boardLikeRepository;

    /**
     *  공고 좋아요테이블에 좋아요내역 추가 및 공고테이블 좋아요카운트 동기화
     *  주의 동시성 발생할 가능성 있음
     *
     * @param boardId
     * @param memberId
     *
     * @return void
     */
    @Override
    @Transactional
    public void createLikesOnBoard(Long boardId, Long memberId) {

        Member member = memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));

        //  exists 랑 find 랑 뭘로 구별하는게 좋은지
        BoardLike findBoardList = boardLikeRepository.findByBoardIdAndMemberId(boardId,memberId);

        if(findBoardList!=null){
            throw new ApplicationException(ALREADY_LIKE_BOARD);
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
     * @param memberId
     */
    @Override
    @Transactional
    public void deleteLikesOnBoard(Long boardLikeId, Long memberId) {
        memberJpaRepository.findById(memberId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        //좋아요 삭제 시 해당 boardLikeCount -=1

        boardLikeRepository.deleteById(boardLikeId);
    }
}
