package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.profile.entity.TechStackItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TechStackItemRepository extends JpaRepository<TechStackItem,Long>, TechStackItemQuerydslRepository {
}