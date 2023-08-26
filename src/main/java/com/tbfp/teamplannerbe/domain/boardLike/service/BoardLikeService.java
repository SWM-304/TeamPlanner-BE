package com.tbfp.teamplannerbe.domain.boardLike.service;

import com.tbfp.teamplannerbe.domain.boardLike.dto.BoardLikeResponseDto;
import com.tbfp.teamplannerbe.domain.boardLike.dto.BoardLikeResponseDto.boardStateResponseDto;

public interface BoardLikeService {
    void createLikesOnBoard(Long boardId, String username);

    void deleteLikesOnBoard(Long boardLikeId, String username);

    boardStateResponseDto getLikeState(Long boardId,String username);
}
