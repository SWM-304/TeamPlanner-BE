package com.tbfp.teamplannerbe.domain.member.repository;

import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.entity.QMember;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;


@Repository
public class MemberRepository extends Querydsl4RepositorySupport{

    public MemberRepository() {
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

    public void updateMemberPassword(Member member, String password){
        QMember qMember = QMember.member; // Querydsl의 Q 클래스를 사용하여 쿼리 작성

        // 쿼리 실행
        update(qMember)
                .set(qMember.password, password) // 비밀번호 업데이트
                .where(qMember.id.eq(member.getId())) // 회원 ID로 조건 설정
                .execute();
    }
}
