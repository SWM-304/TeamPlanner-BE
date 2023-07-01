package com.tbfp.teamplannerbe.domain.member.repository;

import com.tbfp.teamplannerbe.domain.auth.ProviderType;
import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;


@Repository
public class MemberRepository extends Querydsl4RepositorySupport {

    public MemberRepository() {
        super(Member.class);
    }

    public List<Member> basicSelect() {
        return select(member)
                .from(member)
                .fetch();
    }


    public Optional<Member> findMemberByLoginId(String loginId) {
        return Optional.ofNullable(select(member)
                .from(member)
                .where(member.loginId.eq(loginId).and(member.state.eq(true))).
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

    public void updateMemberStateFalseByLoginId(String loginId){
        update(member)
                .set(member.state, false)
                .where(member.loginId.eq(loginId))
                .execute();
    }
}
