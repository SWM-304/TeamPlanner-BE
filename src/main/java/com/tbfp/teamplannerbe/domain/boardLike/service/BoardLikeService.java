package com.tbfp.teamplannerbe.domain.boardLike.service;

public interface BoardLikeService {
    void createLikesOnBoard(Long boardId, Long memberId);

    void deleteLikesOnBoard(Long boardLikeId, Long memberId);
}
