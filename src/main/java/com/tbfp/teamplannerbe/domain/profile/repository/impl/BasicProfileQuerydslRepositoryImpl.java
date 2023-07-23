package com.tbfp.teamplannerbe.domain.profile.repository.impl;

import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.profile.entity.BasicProfile;
import com.tbfp.teamplannerbe.domain.profile.repository.BasicProfileQuerydslRepository;
import org.springframework.stereotype.Repository;

@Repository
public class BasicProfileQuerydslRepositoryImpl extends Querydsl4RepositorySupport implements BasicProfileQuerydslRepository {

    public BasicProfileQuerydslRepositoryImpl() {super(BasicProfile.class);}
}
