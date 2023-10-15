package com.tbfp.teamplannerbe.domain.board.repository.impl;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tbfp.teamplannerbe.domain.board.dto.BoardResponseDto;
import com.tbfp.teamplannerbe.domain.board.dto.BoardSearchCondition;
import com.tbfp.teamplannerbe.domain.board.dto.QBoardResponseDto_BoardSimpleListResponseDto;
import com.tbfp.teamplannerbe.domain.board.entity.Board;
import com.tbfp.teamplannerbe.domain.board.entity.BoardStateEnum;
import com.tbfp.teamplannerbe.domain.board.repository.BoardQuerydslRepository;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Stream;

import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static com.tbfp.teamplannerbe.domain.comment.entity.QComment.comment;
import static org.springframework.util.StringUtils.hasText;


public class BoardQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements BoardQuerydslRepository{


    public BoardQuerydslRepositoryImpl() {
        super(Board.class);
    }


    @Override
    public Page<BoardResponseDto.BoardSimpleListResponseDto> getBoardList(BoardSearchCondition condition, Pageable pageable) {

        String[] word = (condition.getActivityField()!=null) ? condition.getActivityField().split("/") : null;
        BooleanExpression activityFieldExpression = (word != null) ? activityFieldContains(word) : null;


        JPAQuery<BoardResponseDto.BoardSimpleListResponseDto> contentQuery = new JPAQueryFactory(getEntityManager())
                .select(new QBoardResponseDto_BoardSimpleListResponseDto(
                        board.id,
                        board.activityName,
                        board.activityImg,
                        board.category,
                        board.view,
                        board.likeCount,
                        board.recruitmentPeriod,
                        Expressions.numberTemplate(Integer.class, "DATEDIFF(CURRENT_DATE, STR_TO_DATE({0}, '%y.%m.%d'))", board.recruitmentPeriod).as("deadlineInDays"), // Change to Integer.class
                        board.comments.size()
                ))
                .from(board)
                .where(categoryEq(condition.getCategory()), recruitmentPeriodPredicate(), activityFieldExpression)
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        JPAQuery<Long> countQuery = select(board.count())
                .from(board)
                .where(categoryEq(condition.getCategory()),recruitmentPeriodPredicate(),activityFieldExpression);

        return PageableExecutionUtils.getPage(contentQuery.fetch(),pageable,()->countQuery.fetchCount());
    }


    @Override
    public List<Board> getBoardAndComment(Long boardId) {
        List<Board> boardList =
                select(board)
                .from(board)
                .where(boardIdEq(boardId))
                .leftJoin(board.comments, comment).fetchJoin()
                .distinct() // 중복 결과 제거
                .fetch();
        return boardList;
    }

    @Override
    public Page<Board> searchBoardList(String searchWord, Pageable pageable,BoardSearchCondition boardSearchCondition) {



        JPAQuery<Board> contentQuery = selectFrom(board)
                .where(searchWordExpression(searchWord),boardStateEq(boardSearchCondition.getBoardState()))
                .orderBy(getOrderSpecifier(pageable.getSort()).stream().toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize());


        JPAQuery<Long> countQuery = select(board.count())
                .from(board)
                .where(searchWordExpression(searchWord),boardStateEq(boardSearchCondition.getBoardState()));

        return PageableExecutionUtils.getPage(contentQuery.fetch(),pageable,()->countQuery.fetchCount());
    }


    /**
     * 진행 , 마감 중 어떤 값이 들어오든 동적으로 검색해주는
     */

    private BooleanExpression boardStateEq(List<BoardStateEnum> boardStates) {
        if (boardStates == null || boardStates.isEmpty()) {
            return null;
        }
        return boardStates.stream()
                .map(state -> {
                    if (state == BoardStateEnum.ONGOING) {
                        return recruitmentPeriodPredicate();
                    }
                    if (state == BoardStateEnum.CLOSED) {
                        return closedRecruitmentPredicate();
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .reduce(BooleanExpression::or)
                .orElse(null);
    }

    /**
     *  진행 중 활동
     */

    private BooleanExpression recruitmentPeriodPredicate() {
        StringExpression recruitmentPeriodEndDate = Expressions.stringTemplate("STR_TO_DATE(SUBSTRING_INDEX({0}, '~', -1), '%Y.%m.%d')", board.recruitmentPeriod);
        return recruitmentPeriodEndDate.goe(String.valueOf(LocalDate.now()));
    }

    /**
     * 마감 된 활동
     */
    private BooleanExpression closedRecruitmentPredicate() {
        StringExpression recruitmentPeriodEndDate = Expressions.stringTemplate("STR_TO_DATE(SUBSTRING_INDEX({0}, '~', -1), '%Y.%m.%d')", board.recruitmentPeriod);
        return recruitmentPeriodEndDate.lt(String.valueOf(LocalDate.now()));
    }

    /**
     * 동적 orderby
     */

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders=new ArrayList<>();
        
        sort.stream().forEach(order->{
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String property = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Board.class, "board");
            orders.add(new OrderSpecifier(direction,orderByExpression.get(property)));

        });
        return orders;
    }

    /**
     * 카테고리 동적 검색
     */
    private BooleanExpression categoryEq(String category) {
        return hasText(category) ? board.category.eq(category) : null;
    }


    /**
     * 활동영역 동적처리
     */
    private BooleanExpression activityFieldContains(String[] activityFields) {
        BooleanExpression result = null;
        for (String field : activityFields) {
            if (hasText(field)) {
                BooleanExpression fieldExpression = board.activityField.like("%" + field + "%");
                // or 을 처리하여 split 한 word 가 포함되어 있는지 확인해서 넣어줌
                // 예 문학/시나리오  (board.activityField.like("%문학%").or(board.activityField.like("%시나리오%")))
                result = (result == null) ? fieldExpression : result.or(fieldExpression);
            }
        }
        return result;
    }


    /**
     * boardId 동적검색
     */
    private BooleanExpression boardIdEq(Long boardId) {
        return hasText(String.valueOf(boardId)) ? board.id.eq(boardId) : null;
    }

    /**
     * 검색어 단어를 통한 동적검색
     */
    private BooleanExpression searchWordExpression(String searchWord) {

        return Optional.ofNullable(searchWord) //seachWord가 null이 아닌경우에 Optional로 감싸기
                .filter(word->!word.isEmpty()) // searchWord가 비어 있지 않은경우에만 map 함수
                .map(word-> Stream.of(board.activityName.containsIgnoreCase(word),
                                      board.activityDetail.containsIgnoreCase(word),
                                      board.activityField.containsIgnoreCase(word))
                        .reduce(BooleanExpression::or) // 위 조건들을 OR 연산으로 묶음
                        .orElse(null))
                .orElse(null);//  // 만약 조건이 없으면 null 반환

    }






}
