package com.tbfp.teamplannerbe.domain.board.repository;


import com.querydsl.core.types.dsl.BooleanExpression;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static org.springframework.util.StringUtils.hasText;


@Repository
public class BoardRepository extends Querydsl4RepositorySupport{


    @PersistenceContext
    private EntityManager entityManager;

    public BoardRepository() {
        super(Board.class);
    }

    public List<Board> basicSelect() {
        return select(board)
                .from(board)
                .fetch();
    }

    public List<Board> getBoardListbyCategory(String Category){
        return select(board)
                .from(board)
                .where(Category!=null ? board.category.eq(Category) : board.category.isNull())
                .fetch();
    }

    /**
     *
     * 주의 할 점 :: eq 안에는 null값이 들어가선 안됨.
     */
    public Board findById(Long id) {
        return select(board)
                .from(board)
                .where(id!=null ? board.id.eq(id) : board.id.isNull())
                .fetchOne();
    }

    public Board findByactivitykey(String activity_key) {
        return select(board)
                .from(board)
                .where(activity_key!=null ? board.activity_key.eq(activity_key) : board.id.isNull())
                .fetchOne();
    }
    public Board save(Board board) {
        entityManager.persist(board);
        return board;
    }


    public Page<Board> applyPagination(BoardSearchCondition condition, Pageable pageable){
        Page<Board> result = applyPagination(pageable, contentQuery -> contentQuery.selectFrom(board)
                .where(categoryEq(condition.getCategory())),countQuery->countQuery
                .select(board.id)
                .from(board)
                .where(categoryEq(condition.getCategory()))
        );
        return result;
    }


    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? board.category.eq(category) : null;
    }


}