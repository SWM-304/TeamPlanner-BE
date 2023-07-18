package com.tbfp.teamplannerbe.domain.comment.service;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.createCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.updateCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.commentToCommentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.createdCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.createdchildCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.updatedCommentResponseDto;

public interface CommentService {
    createdCommentResponseDto sendComment(createCommentRequestDto createCommentRequestDto);

    void deleteComment(Long boardId, Long commentId);

    updatedCommentResponseDto updateComment(updateCommentRequestDto updateCommentRequestDto);

    createdchildCommentResponseDto sendBigComment(commentToCommentCreateRequestDto commentToCommentCreateRequestDto, String username);
}
