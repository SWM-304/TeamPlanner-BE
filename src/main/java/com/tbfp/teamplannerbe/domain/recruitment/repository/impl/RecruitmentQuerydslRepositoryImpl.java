package com.tbfp.teamplannerbe.domain.recruitment.repository.impl;

import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.recruitment.condition.RecruitmentSearchCondition;
import com.tbfp.teamplannerbe.domain.recruitment.dto.QRecruitmentResponseDto_RecruitmentSearchDto;
import com.tbfp.teamplannerbe.domain.recruitment.dto.RecruitmentResponseDto.RecruitmentSearchDto;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import com.tbfp.teamplannerbe.domain.recruitment.repository.RecruitmentQuerydslRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;
import static com.tbfp.teamplannerbe.domain.recruitment.entity.QRecruitment.recruitment;
import static org.springframework.util.StringUtils.hasText;

public class RecruitmentQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements RecruitmentQuerydslRepository {
    public RecruitmentQuerydslRepositoryImpl() {
        super(Recruitment.class);
    }

    @Override
    public Page<RecruitmentSearchDto> searchPage(RecruitmentSearchCondition recruitmentSearchCondition, Pageable pageable) {
        return applyPagination(pageable, contentQuery -> contentQuery
                        .select(new QRecruitmentResponseDto_RecruitmentSearchDto(
                                recruitment.id,
                                recruitment.title,
                                recruitment.content,
                                recruitment.viewCount,
                                recruitment.likeCount,
                                recruitment.currentMemberSize,
                                recruitment.maxMemberSize,
                                recruitment.createdAt,
                                recruitment.author.nickname,
                                recruitment.author.basicProfile.profileImage,
                                recruitment.commentList.size(),
                                recruitment.board.recruitmentPeriod,
                                recruitment.board.activityName,
                                recruitment.board.activityImg,
                                recruitment.board.activityField,
                                recruitment.board.category
                                ))
                        .from(recruitment)
                        .leftJoin(recruitment.board, board)
                        .leftJoin(recruitment.author, member)
                        .where(
                                titleContain(recruitmentSearchCondition.getTitleContain()),
                                contentContain(recruitmentSearchCondition.getContentContain()),
                                authorNameContain(recruitmentSearchCondition.getAuthorNameContain()),
                                boardTitleContain(recruitmentSearchCondition.getBoardTitleContain()),
                                boardIdContain(recruitmentSearchCondition.getBoardIdContain()),
                                boardCategoryContain(recruitmentSearchCondition.getBoardCategoryContain())
                        )
        );
    }
    private BooleanExpression boardCategoryContain(String boardCategoryContain) {
        return hasText(boardCategoryContain) ? recruitment.board.category.contains(boardCategoryContain) : null;
    }
    private BooleanExpression boardIdContain(Long boardIdContain) {
        return (boardIdContain != null) ? recruitment.board.id.eq(boardIdContain) : null;
    }

    private BooleanExpression titleContain(String titleContain) {
        return hasText(titleContain) ? recruitment.title.contains(titleContain) : null;
    }

    private BooleanExpression contentContain(String contentContain) {
        return hasText(contentContain) ? recruitment.content.containsIgnoreCase(contentContain) : null;
    }

    private BooleanExpression authorNameContain(String authorNameContain) {
        return hasText(authorNameContain) ? recruitment.author.username.containsIgnoreCase(authorNameContain) : null;
    }

    private BooleanExpression boardTitleContain(String boardTitleContain) {
        return hasText(boardTitleContain) ? recruitment.board.activityName.containsIgnoreCase(boardTitleContain) : null;
    }
}
