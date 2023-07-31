package com.tbfp.teamplannerbe.domain.comment.service;

import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CreateCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.UpdateCommentRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentRequestDto.CommentToCommentCreateRequestDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.CreatedCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.CreatedchildCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.UpdatedCommentResponseDto;
import com.tbfp.teamplannerbe.domain.comment.dto.CommentResponseDto.commentToCommentListResponseDto;

import java.util.List;

public interface CommentService {
    CreatedCommentResponseDto sendComment(CreateCommentRequestDto createCommentRequestDto);

    void deleteComment(Long boardId, Long commentId);

    UpdatedCommentResponseDto updateComment(UpdateCommentRequestDto updateCommentRequestDto);

    CreatedchildCommentResponseDto sendBigComment(CommentToCommentCreateRequestDto commentToCommentCreateRequestDto, String username);

    List<commentToCommentListResponseDto> getCommentToCommentList(Long boardId, Long commentId);
}
