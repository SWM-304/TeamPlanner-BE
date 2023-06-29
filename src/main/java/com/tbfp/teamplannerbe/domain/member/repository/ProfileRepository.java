package com.tbfp.teamplannerbe.domain.member.repository;

import com.tbfp.teamplannerbe.domain.common.querydsl.support.Querydsl4RepositorySupport;
import com.tbfp.teamplannerbe.domain.member.entity.Profile;
import org.springframework.stereotype.Repository;

@Repository
public class ProfileRepository extends Querydsl4RepositorySupport {

    public ProfileRepository() {super(Profile.class);}
    public Profile save(Profile profile) {
        getEntityManager().persist(profile);
        return profile;
    }
}
