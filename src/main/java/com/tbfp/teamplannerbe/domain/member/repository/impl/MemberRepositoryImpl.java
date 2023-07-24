package com.tbfp.teamplannerbe.domain.member.repository.impl;

import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberQuerydslRepository;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;

import java.util.List;
import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;
import static com.tbfp.teamplannerbe.domain.recruitment.entity.QRecruitment.recruitment;
import static com.tbfp.teamplannerbe.domain.recruitmentApply.entity.QRecruitmentApply.recruitmentApply;

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
                .fetch();
//        JPAQuery<Long> countQuery =
//                select(recruitment.count()).
//                        from(recruitment).
//                        leftJoin(recruitment.board, board).fetchJoin()
//                        .leftJoin(recruitment.author, member).fetchJoin()
//                        .leftJoin(recruitment.recruitmentApplyList, recruitmentApply).fetchJoin()
//                        .where(recruitment.author.username.eq(username));
//
//        return PageableExecutionUtils.getPage(content, pageable, countQuery::fetchOne);
        return content;

    }
}
