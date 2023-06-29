package com.tbfp.teamplannerbe.domain.auth.repository;

import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
