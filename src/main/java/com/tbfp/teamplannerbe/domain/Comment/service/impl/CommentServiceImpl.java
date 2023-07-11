package com.tbfp.teamplannerbe.domain.comment.service.impl;


import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.comment.repository.CommentJpaRepository;
import com.tbfp.teamplannerbe.domain.comment.repository.CommentRepository;
import com.tbfp.teamplannerbe.domain.comment.service.CommentService;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentJpaRepository commentJpaRepository;
//
//    @PersistenceContext
//    private EntityManager em;

    /**
     *
     * @param
     * {
     *     "board_Id":"1",
     *     "content":"안녕",
     *     "member_Id":"test2"
     * }
     * @return
     * commnetId 예) 1
     */
    @Override
    @Transactional
    public Long sendComment(CommentRequestDto.CommentSendRequestDto commentSendRequestDto) {
        Member member = memberRepository.findMemberByLoginId(commentSendRequestDto.getMemberId())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.findById(commentSendRequestDto.getBoardId())
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FIND));

        Comment comment = Comment.builder()
                .content(commentSendRequestDto.getContent())
                .state(true)  // 막 생성 된거니 state true
                .board(board)
                .depth(1)
                .member(member)
                .isConfidential(false)
                .build();
        Comment savedComment = commentRepository.save(comment);
        return savedComment.getId();
    }


    /**
     *
     * localhost:8080/api/v1/comment/delete/1/comment/1
     */

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId) {
        Optional<Board> findBoard = Optional.ofNullable(boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND)));

        Optional<Comment> findComment = Optional.ofNullable(commentRepository.findBycommentId(commentId).
                        orElseThrow(()->new RuntimeException("댓글을 찾을 수 없습니다")));


//        // 공고글 하고 Comment 둘다 있고 부모댓글이라면 삭제가능
//        if (findComment.get().getDepth()==1 && findBoard!=null && findComment!=null){
////            deleteId = commentRepository.deleteComment(commentId);
//
//            // 부모댓글 state 값 false
//            findComment.get().setState(false);
//            // 자식댓글은 queryDsl bulkUpdate를 통해 한꺼번에 처리
//            //주의사항 ** 데이터베이스에 직접 쿼리를 날리기때문에 영속성컨텍스트와 값이 달라질 수 있음 flush 및 clear 필수
//            commentRepository.stateFalseComment(findComment.get().getId());
//
//            commentJpaRepository.save(findComment.get());
//
//            em.flush();
//            em.clear();
//        }
        // 자식댓글일 경우 해결 댓글만 state 값 false
        if(findBoard.isPresent() && findComment.isPresent()){
            findComment.get().setState(false);
            commentJpaRepository.save(findComment.get());
        }

    }


    /**
     *
     * @param commentUpdateRequestDto
     * {
     *     "boardId":"1",
     *     "content":"수정된 글 입니다",
     *     "commentId":"3"
     * }
     * @return
     * comment.getId()
     */
    @Override
    @Transactional
    public CommentResponseDto.updatedCommentResponseDto updateComment(CommentRequestDto.CommentUpdateRequestDto commentUpdateRequestDto) {
        Comment findComment = commentJpaRepository.findByIdAndBoardId(
                commentUpdateRequestDto.getCommentId(),
                commentUpdateRequestDto.getBoardId());

        if(findComment==null){
            throw new RuntimeException("공고글 및 댓글이 존재 하지 않습니다");
        }

        findComment.updateContent(commentUpdateRequestDto.getContent());

        Comment savedComment = commentJpaRepository.save(findComment);

        CommentResponseDto.updatedCommentResponseDto commentResponseDto = CommentResponseDto.updatedCommentResponseDto.builder()
                .content(savedComment.getContent())
                .boardId(savedComment.getBoard().getId())
                .memberId(savedComment.getMember().getLoginId())
                .build();

        return commentResponseDto;

    }


    /**
     *
     * @param
     * {
     *     "parentCommentId":5,
     *     "boardId":"1",
     *     "content":"나는 자식댓글4",
     *     "memberId":"test2",
     *     "isConfidential": true
     * }
     *
     *
     */
    @Transactional
    public Long sendBigComment(CommentRequestDto.bigCommentSendRequestDto bigCommentSendRequestDto) {

        Comment childComment=null;
        Member member = memberRepository.findMemberByLoginId(bigCommentSendRequestDto.getMemberId())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.findById(bigCommentSendRequestDto.getBoardId())
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FIND));

        if (bigCommentSendRequestDto.getParentCommentId() != null) {
            Comment parentComment = commentJpaRepository.findById(bigCommentSendRequestDto.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다"));

            log.info(String.valueOf(bigCommentSendRequestDto.isConfidential()));
            childComment = Comment.builder()
                    .content(bigCommentSendRequestDto.getContent())
                    .state(true)
                    .board(board)
                    .member(member)
                    .isConfidential(true)
                    .build();

            parentComment.setParentComment(childComment);

            commentRepository.save(parentComment);  // 부모 댓글 저장

            childComment.setParentComment(parentComment);

            commentRepository.save(childComment);  // 대댓글 저장

        }
        return childComment.getId();
    }
}
