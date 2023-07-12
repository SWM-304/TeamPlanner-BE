package com.tbfp.teamplannerbe.domain.board.repository;

import com.tbfp.teamplannerbe.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board,Long>,BoardQuerydslRepository {
    Optional<Board> findByActivityKey(String activityKey);

//    @EntityGraph(attributePaths = {"comments"})
//    List<Board> findBoardAndComments(Long boardId);

}
