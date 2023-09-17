package com.tbfp.teamplannerbe.domain.profile.repository;

import com.tbfp.teamplannerbe.domain.profile.entity.TechStackItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TechStackItemRepository extends JpaRepository<TechStackItem,Long>, TechStackItemQuerydslRepository {
    List<TechStackItem> findByUserGeneratedFalse();
}