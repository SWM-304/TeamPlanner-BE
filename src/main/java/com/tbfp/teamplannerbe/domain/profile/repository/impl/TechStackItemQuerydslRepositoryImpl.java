package com.tbfp.teamplannerbe.domain.profile.repository.impl;

import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.profile.entity.TechStackItem;
import com.tbfp.teamplannerbe.domain.profile.repository.TechStackItemQuerydslRepository;

import java.util.List;

public class TechStackItemQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements TechStackItemQuerydslRepository {
    public TechStackItemQuerydslRepositoryImpl() {
        super(TechStackItem.class);
    }

//    @Override
//    public List<TechStackItem> basicSelect() {
//        return null;
//    }
}
