package com.tbfp.teamplannerbe.domain.Comment.service;

import com.tbfp.teamplannerbe.domain.Comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.Comment.dto.CommentResponseDto;

public interface CommentService {
    Long sendComment(CommentRequestDto.CommentSendRequestDto commentSendRequestDto);

    void deleteComment(Long boardId, Long commentId);

    CommentResponseDto.updatedCommentResponseDto updateComment(CommentRequestDto.CommentUpdateRequestDto commentUpdateRequestDto);

    Long sendBigComment(CommentRequestDto.bigCommentSendRequestDto bigCommentSendRequestDto);
}
