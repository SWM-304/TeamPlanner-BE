package com.tbfp.teamplannerbe.domain.comment.repository;

import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface CommentRepository extends JpaRepository<Comment, Long>,CommentQueryDslRepository{
    Comment findByIdAndBoardId(Long commentId,Long boardId);

    List<Comment> findAllById(Long commentId);
}
