package com.tbfp.teamplannerbe.domain.profile.repository.impl;

import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.profile.entity.TechStack;
import com.tbfp.teamplannerbe.domain.profile.repository.TechStackQuerydslRepository;

import java.util.List;
import java.util.Optional;

import static com.tbfp.teamplannerbe.domain.profile.entity.QTechStack.techStack;
import static com.tbfp.teamplannerbe.domain.profile.entity.QTechStackItem.techStackItem;

public class TechStackQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements TechStackQuerydslRepository {
    public TechStackQuerydslRepositoryImpl() {
        super(TechStack.class);
    }

    public Optional<List<TechStack>> findAllByMemberId(Long memberId){
        return Optional.ofNullable(selectFrom(techStack)
                .where(techStack.member.id.eq(memberId))
                .leftJoin(techStack.techStackItem, techStackItem).fetchJoin()
                .fetch());
    }
}
