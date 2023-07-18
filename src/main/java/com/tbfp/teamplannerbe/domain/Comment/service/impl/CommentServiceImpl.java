package com.tbfp.teamplannerbe.domain.comment.service.impl;


import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.createCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.updateCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.commentToCommentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.createdCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.createdchildCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.updatedCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.comment.repository.CommentRepository;
import com.tbfp.teamplannerbe.domain.comment.service.CommentService;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
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

    private final BoardRepository boardRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
//
//    @PersistenceContext
//    private EntityManager em;

    /**
     *
     * @param
     * {
     *     "boardId":"1",
     *     "content":"안녕",
     *     "memberId":"test2"
     * }
     * @return
     * commnetId 예) 1
     */
    @Override
    @Transactional
    public createdCommentResponseDto sendComment(createCommentRequestDto createCommentRequestDto) {
        Member member = memberRepository.findMemberByUsername(createCommentRequestDto.getMemberId())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.findById(createCommentRequestDto.getBoardId())
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));

        Comment comment = Comment.builder()
                .content(createCommentRequestDto.getContent())
                .state(true)  // 막 생성 된거니 state true
                .board(board)
                .depth(1)
                .member(member)
                .isConfidential(createCommentRequestDto.getIsConfidential())
                .build();
        Comment savedComment = commentRepository.save(comment);
        createdCommentResponseDto commentResponseDto = savedComment.toDto();
        return commentResponseDto;
    }


    /**
     *
     * localhost:8080/api/v1/comment/delete/1/comment/1
     */

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId) {
        Board board = boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));

        Comment findComment=commentRepository.findById(commentId).
                orElseThrow(()->new ApplicationException(COMMENT_NOT_FOUND));


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
        if(findComment.isState()){
            findComment.changeState(false);
            commentRepository.save(findComment);
        }

    }


    /**
     *
     * @param updateCommentRequestDto
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
    public updatedCommentResponseDto updateComment(updateCommentRequestDto updateCommentRequestDto) {

        Comment findComment = commentRepository.findByIdAndStateIsTrue(
                updateCommentRequestDto.getCommentId());

        if(findComment==null){
            throw new ApplicationException(COMMENT_NOT_FOUND);
        }

        findComment.updateContent(updateCommentRequestDto.getContent());

        Comment savedComment = commentRepository.save(findComment);

        updatedCommentResponseDto commentResponseDto = updatedCommentResponseDto.builder()
                .content(savedComment.getContent())
                .boardId(savedComment.getBoard().getId())
                .memberId(savedComment.getMember().getUsername())
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
    // 대댓글 달 수 있는 조건?
    // 내가 그 댓글 볼 수 있어야함 ( 내가 쓴 댓글이거나 남이 썻는데 confidential 이 아님)
    // 또는 내가 모집글의 작성자임
    @Transactional
    public createdchildCommentResponseDto sendBigComment(commentToCommentCreateRequestDto commentToCommentCreateRequestDto, String username) {




        Comment childComment=null;
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.findById(commentToCommentCreateRequestDto.getBoardId())
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));
        Optional<Comment> findParentComment = commentRepository.findById(commentToCommentCreateRequestDto.getParentCommentId());

        boolean isAccessible = board.getMember().getUsername().equals(member.getUsername())
                || (findParentComment.get().isConfidential() && findParentComment.get().getMember().getUsername().equals(username));
        if (!isAccessible){
            throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);
        }



        if (commentToCommentCreateRequestDto.getParentCommentId() != null) {
            Comment parentComment = commentRepository.findById(commentToCommentCreateRequestDto.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("부모 댓글을 찾을 수 없습니다"));





            childComment = Comment.builder()
                    .content(commentToCommentCreateRequestDto.getContent())
                    .state(true)
                    .board(board)
                    .member(member)
                    .isConfidential(commentToCommentCreateRequestDto.getIsConfidential())
                    .build();

            if(parentComment.getId()!=childComment.getId()){
                parentComment.setParentComment(childComment);
            }

//            System.out.println("=================");
//            commentRepository.save(parentComment);  // 부모 댓글 저장
//            System.out.println("=================");
            childComment.setParentComment(parentComment);

            commentRepository.save(childComment);  // 대댓글 저장

        }
        CommentResponseDto.createdchildCommentResponseDto commentToComment = createdchildCommentResponseDto.builder()
                .boardId(childComment.getBoard().getId())
                .commentId(childComment.getId())
                .parentId(childComment.getParentComment().getId())
                .createdDate(childComment.getCreatedAt())
                .content(childComment.getContent())
                .username(childComment.getMember().getUsername())
                .isConfidential(String.valueOf(childComment.isConfidential()))
                .build();


        return commentToComment;
    }
}
