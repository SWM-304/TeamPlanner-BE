package com.tbfp.teamplannerbe.domain.boardLike.repository;

import com.tbfp.teamplannerbe.domain.boardLike.entity.BoardLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BoardLikeRepository extends JpaRepository<BoardLike,Long> {
    BoardLike findByBoardIdAndMemberId(Long boardId, Long memberId);

}
