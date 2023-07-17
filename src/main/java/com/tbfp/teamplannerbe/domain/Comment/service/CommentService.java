package com.tbfp.teamplannerbe.domain.comment.service;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CommentSendRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CommentUpdateRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.bigCommentSendRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.updatedCommentResponseDto;

public interface CommentService {
    Long sendComment(CommentSendRequestDto commentSendRequestDto);

    void deleteComment(Long boardId, Long commentId);

    updatedCommentResponseDto updateComment(CommentUpdateRequestDto commentUpdateRequestDto);

    Long sendBigComment(bigCommentSendRequestDto bigCommentSendRequestDto);
}
