package com.tbfp.teamplannerbe.domain.member.repository.impl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.ExpressionUtils;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.ListExpression;
import com.querydsl.core.types.dsl.SimpleExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.board.dto.QBoardResponseDto_BoardSimpleListResponseDto;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.member.dto.MemberDto;
import com.tbfp.teamplannerbe.domain.member.dto.QMemberDto_ProfileInfoForScoringDto;
import com.tbfp.teamplannerbe.domain.member.dto.QMemberDto_ProfileInfoForScoringDto_TechStackItemDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.entity.QMember;
import com.tbfp.teamplannerbe.domain.member.repository.MemberQuerydslRepository;
import com.tbfp.teamplannerbe.domain.profile.dto.ProfileResponseDto;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;

import java.util.*;

import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;
import static com.tbfp.teamplannerbe.domain.profile.entity.QActivity.activity;
import static com.tbfp.teamplannerbe.domain.profile.entity.QCertification.certification;
import static com.tbfp.teamplannerbe.domain.profile.entity.QEvaluation.evaluation;
import static com.tbfp.teamplannerbe.domain.profile.entity.QTechStack.techStack;
import static com.tbfp.teamplannerbe.domain.profile.entity.QTechStackItem.techStackItem;
import static com.tbfp.teamplannerbe.domain.recruitment.entity.QRecruitment.recruitment;
import static com.tbfp.teamplannerbe.domain.recruitmentApply.entity.QRecruitmentApply.recruitmentApply;
import static org.hibernate.internal.util.NullnessHelper.coalesce;

public class MemberRepositoryImpl extends Querydsl4RepositorySupport implements MemberQuerydslRepository {
    public MemberRepositoryImpl() {
        super(Member.class);
    }

    public List<Member> basicSelect() {
        return select(member)
                .from(member)
                .fetch();
    }

    /**
     *
     * username username가 null값이 들어오면 오류뜸 예외처리 필요
     *
     */

    public Optional<Member> findMemberByUsername(String username) {
        return Optional.ofNullable(select(member)
                .from(member)
                .where(member.username.eq(username).and(member.state.eq(true))).
                fetchOne());
    }

    public Optional<Member> findByProviderTypeAndProviderId(ProviderType providerType, String providerId) {
        return Optional.ofNullable(
                selectFrom(member)
                        .where(member.providerType.eq(providerType).and(member.providerId.eq(providerId)))
                        .fetchOne()
        );
    }

    public Member save(Member member) {
        getEntityManager().persist(member);
        return member;
    }

    public void updateMemberStateFalseByUsername(String username){
        update(member)
                .set(member.state, false)
                .where(member.username.eq(username))
                .execute();
    }

    public Optional<List<String>> findUsernamesByEmail(String email) {
        return Optional.ofNullable(select(member.username)
                .from(member)
                .where(member.email.eq(email).and(member.state.eq(true))).
                fetch());
    }

    public void updateMemberPassword(Member theMember, String password){

        // 쿼리 실행
        update(member)
                .set(member.password, password) // 비밀번호 업데이트
                .where(member.id.eq(theMember.getId())) // 회원 ID로 조건 설정
                .execute();
    }

    public List<Recruitment> getApplicantList(String username) {
        List<Recruitment> content = selectFrom(recruitment)
                .leftJoin(recruitment.board, board).fetchJoin()
                .leftJoin(recruitment.author, member).fetchJoin()
                .leftJoin(recruitment.recruitmentApplyList, recruitmentApply).fetchJoin()
                .where(recruitment.author.username.eq(username))
                .distinct() // 중복 데이터 제거
                .fetch();
        return content;
    }
    @Override
    public MemberDto.ProfileInfoForScoringDto findProfileInfoForScoring(Long memberId){
        return select(
                Projections.constructor(
                        MemberDto.ProfileInfoForScoringDto.class,
                        member.id,
                        member.basicProfile.job,
                        member.basicProfile.education,
                        member.basicProfile.admissionDate,
                        member.basicProfile.birth,
                        member.basicProfile.address,
                        //List<String> activitySubjects : activity는 member와 many to one mapping, member의 activty.subject 가져오기
                        Expressions.constant(
                                select(
                                        Projections.constructor(
                                                MemberDto.ProfileInfoForScoringDto.TechStackItemDto.class,
                                                techStack.techStackItem.id,
                                                techStack.techStackItem.name,
                                                techStack.skillLevel
                                        ))
                                        .from(techStack)
                                        .where(techStack.member.id.eq(memberId))
                                        .leftJoin(techStack.techStackItem,techStackItem)
                                        .leftJoin(techStack.member,member)
                                        .fetch()
                        ),
                        Expressions.constant(
                                select(activity.subject)
                                        .from(activity)
                                        .where(activity.member.id.eq(memberId))
                                        .leftJoin(activity.member,member)
                                        .fetch()
                        ),
                        Expressions.constant(
                                select(certification.name)
                                        .from(certification)
                                        .where(certification.member.id.eq(memberId))
                                        .leftJoin(certification.member,member)
                                        .fetch()
                        ),
                        Expressions.constant(0.0)
                ))
                .from(member)
                .where(member.state.eq(true).and(member.id.eq(memberId)))
                .fetchOne();
    }

    @Override
    public ProfileResponseDto.RecommendedUserResponseDto getRecommendedUserResponseDto(Long id, List<String> similarities){
        return select(
                Projections.constructor(
                        ProfileResponseDto.RecommendedUserResponseDto.class,
                        member.id,
                        member.nickname,
                        member.basicProfile.profileIntro,
                        member.basicProfile.profileImage,
                        Expressions.constant(similarities)
                ))
                .from(member)
                .where(member.id.eq(id).and(member.state.eq(true)))
                .fetchOne();
    }
    //Long id, String nickname, String profileIntro, String profileImage, List<String> similarities
}
