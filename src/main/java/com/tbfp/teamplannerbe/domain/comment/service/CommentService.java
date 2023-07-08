package com.tbfp.teamplannerbe.domain.comment.service;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;

public interface CommentService {
    Long sendComment(CommentRequestDto.CommentSendRequestDto commentSendRequestDto);

    void deleteComment(Long boardId, Long commentId);

    CommentResponseDto.updatedCommentResponseDto updateComment(CommentRequestDto.CommentUpdateRequestDto commentUpdateRequestDto);

    Long sendBigComment(CommentRequestDto.bigCommentSendRequestDto bigCommentSendRequestDto);
}
