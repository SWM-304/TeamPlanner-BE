package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.profile.entity.TechStack;

import java.util.List;
import java.util.Optional;

public interface TechStackQuerydslRepository {
    Optional<List<TechStack>> findAllByMemberId(Long memberId);
}
