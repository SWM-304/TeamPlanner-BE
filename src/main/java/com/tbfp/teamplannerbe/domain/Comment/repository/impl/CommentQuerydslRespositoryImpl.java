package com.tbfp.teamplannerbe.domain.comment.repository.impl;

import com.tbfp.teamplannerbe.domain.board.repository.BoardQuerydslRepository;
import com.tbfp.teamplannerbe.domain.comment.entity.Comment;
import com.tbfp.teamplannerbe.domain.comment.repository.CommentQueryDslRepository;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;

public class CommentQuerydslRespositoryImpl extends Querydsl4RepositorySupport implements CommentQueryDslRepository {
    public CommentQuerydslRespositoryImpl() {
        super(Comment.class);
    }
}
