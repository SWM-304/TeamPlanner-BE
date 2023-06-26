package com.tbfp.teamplannerbe.domain.auth.repository;

import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, Long> {
    Optional<RefreshToken> findRefreshTokenByMember_LoginId(String loginId);
}
