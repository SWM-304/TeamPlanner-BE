package com.tbfp.teamplannerbe.domain.boardLike.repository.impl;

import com.tbfp.teamplannerbe.domain.boardLike.entity.BoardLike;
import com.tbfp.teamplannerbe.domain.boardLike.repository.BoardLikeQuerydslRepository;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;


public class BoardLikeQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements BoardLikeQuerydslRepository {

    public BoardLikeQuerydslRepositoryImpl() {
        super(BoardLike.class);
    }
}
