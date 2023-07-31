package com.tbfp.teamplannerbe.domain.comment.repository;

import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import io.lettuce.core.dynamic.annotation.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long>,CommentQueryDslRepository{

    Comment findByIdAndStateIsTrue(Long commentId);

    List<Comment> findAllByBoard_IdAndParentCommentId(Long boardId,Long commentId);




}
