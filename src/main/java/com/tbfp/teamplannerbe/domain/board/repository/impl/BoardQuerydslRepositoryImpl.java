package com.tbfp.teamplannerbe.domain.board.repository.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.repository.BoardQuerydslRepository;
import com.tbfp.teamplannerbe.domain.comment.entity.QComment;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.util.ArrayList;
import java.util.List;

import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static com.tbfp.teamplannerbe.domain.comment.entity.QComment.comment;
import static org.springframework.util.StringUtils.hasText;


public class BoardQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements BoardQuerydslRepository{


    public BoardQuerydslRepositoryImpl() {
        super(Board.class);
    }


    @Override
    public Page<Board> getBoardList(BoardSearchCondition condition, Pageable pageable) {

        List<Board> content=selectFrom(board)
                .leftJoin(board.comments,comment).fetchJoin()
                .where(categoryEq(condition.getCategory()))
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Long> countQuery = select(board.count())
                .from(board)
                .where(categoryEq(condition.getCategory()));

        return PageableExecutionUtils.getPage(content,pageable,countQuery::fetchOne);
    }

    @Override
    public List<Board> getBoardAndComment(Long boardId) {
        List<Board> boardList =
                select(board)
                .from(board)
                .leftJoin(board.comments, comment).fetchJoin()
                .where(boardIdEq(boardId))
                .distinct() // 중복 결과 제거
                .fetch();
        return boardList;
    }


    /**
     * 동적 orderby
     */

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders=new ArrayList<>();
        
        sort.stream().forEach(order->{
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            System.out.println("order"+direction);
            String property = order.getProperty();
            System.out.println("property"+property);
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            System.out.println("orderByExpression"+orderByExpression);

            orders.add(new OrderSpecifier(direction,orderByExpression.get(property)));

        });
        return orders;
    }

    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? board.category.eq(category) : null;
    }
    private BooleanExpression boardIdEq(Long boardId) {
        return hasText(String.valueOf(boardId)) ? board.id.eq(boardId) : null;
    }



}
