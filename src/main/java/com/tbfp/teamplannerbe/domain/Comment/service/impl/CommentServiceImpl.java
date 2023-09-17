package com.tbfp.teamplannerbe.domain.comment.service.impl;


import com.tbfp.teamplannerbe.domain.board.repository.BoardRepository;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CreateCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.UpdateCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CommentToCommentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.CreatedCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.CreatedchildCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.UpdatedCommentResponseDto;
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

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
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
    public CreatedCommentResponseDto sendComment(CreateCommentRequestDto createCommentRequestDto) {
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

        CreatedCommentResponseDto commentResponseDto = savedComment.toDto();
        return commentResponseDto;
    }


    /**
     *
     * localhost:8080/api/v1/comment/delete/1/comment/1
     */

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId) {
        boardRepository.findById(boardId)
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));

        Comment findComment=commentRepository.findById(commentId).
                orElseThrow(()->new ApplicationException(COMMENT_NOT_FOUND));
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
    public UpdatedCommentResponseDto updateComment(UpdateCommentRequestDto updateCommentRequestDto) {

        Comment findComment = commentRepository.findByIdAndStateIsTrue(
                updateCommentRequestDto.getCommentId());

        if(findComment==null){
            throw new ApplicationException(COMMENT_NOT_FOUND);
        }

        findComment.updateContent(updateCommentRequestDto.getContent());

        Comment savedComment = commentRepository.save(findComment);

        UpdatedCommentResponseDto commentResponseDto = UpdatedCommentResponseDto.builder()
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
    public CreatedchildCommentResponseDto sendCommentToComment(CommentToCommentCreateRequestDto commentToCommentCreateRequestDto, String username) {
        Comment childComment=null;
        Member member = memberRepository.findMemberByUsername(username)
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));

        Board board = boardRepository.findById(commentToCommentCreateRequestDto.getBoardId())
                .orElseThrow(() -> new ApplicationException(BOARD_NOT_FOUND));
        Optional<Comment> findParentComment = commentRepository.findById(commentToCommentCreateRequestDto.getParentCommentId());

        //비밀댓글이 아니여야하고
        //isAccessible 변수는 다음 조건 중 하나를 만족할 때 true로 설정
        //현재 사용자가 게시판을 작성한 사용자와 동일한 경우.
        //댓글이 비밀 댓글이 아닌 경우.
        //현재 사용자가 해당 댓글을 작성한 사용자와 동일한 경우.
        commentToCommentValidation(username, member, board, findParentComment);

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

            childComment.setParentComment(parentComment);
            //부모댓글 카운트 동기화
            parentComment.incrementchildCommentCount();
            commentRepository.save(childComment);  // 대댓글 저장


        }
        CreatedchildCommentResponseDto commentToComment = CreatedchildCommentResponseDto.builder()
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

    private static void commentToCommentValidation(String username, Member member, Board board, Optional<Comment> findParentComment) {
        if(board.getMember()==null){
            boolean isAccessible=(!findParentComment.get().isConfidential() || findParentComment.get().getMember().getUsername().equals(username));
            if (!isAccessible){
                throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);
            }
        }
        if(board.getMember()!=null){
            boolean isAccessible = board.getMember().getUsername().equals(member.getUsername())
                    || (!findParentComment.get().isConfidential() || findParentComment.get().getMember().getUsername().equals(username));
            if (!isAccessible){
                throw new ApplicationException(ApplicationErrorType.UNAUTHORIZED);
            }
        }
    }

    @Override
    public List<CommentResponseDto.commentToCommentListResponseDto> getCommentToCommentList(Long boardId, Long commentId) {
        List<Comment> childCommentList = commentRepository.findAllByBoard_IdAndParentCommentId(boardId, commentId);

        List<CommentResponseDto.commentToCommentListResponseDto> result = childCommentList.stream().map(comment -> new CommentResponseDto.commentToCommentListResponseDto(comment))
                .collect(Collectors.toList());

        return result;
    }
}
