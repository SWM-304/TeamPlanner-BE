package com.tbfp.teamplannerbe.domain.auth.repository;

import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
class RefreshTokenRepositoryTest {

    @Autowired
    private RefreshTokenRepository refreshTokenRedisRepository;

    @AfterEach
    void tearDown() {
//        refreshTokenRedisRepository.deleteAll();
    }

    @Test
    void redisSetAndGet() {
        String loginId = "member1";
        String tokenValue = "token value";

        RefreshToken token = RefreshToken.builder().id(loginId).token(tokenValue).build();
        refreshTokenRedisRepository.save(token);

        RefreshToken foundToken = refreshTokenRedisRepository.findById(loginId).get();

        assertThat(foundToken.getToken()).isEqualTo(tokenValue);
    }
    @Test
    void overwrite() {
        String loginId = "member1";
        String tokenValue1 = "token value1";
        String tokenValue2 = "token value2";

        RefreshToken token = RefreshToken.builder().token(tokenValue1).id(loginId).build();
        refreshTokenRedisRepository.save(token);
        RefreshToken token2 = RefreshToken.builder().token(tokenValue2).id(loginId).build();
        refreshTokenRedisRepository.save(token2);

        RefreshToken foundToken = refreshTokenRedisRepository.findById(loginId).get();


        assertThat(foundToken.getToken()).isEqualTo(tokenValue2);
    }

    @Test
    void noUser() {
        Optional<RefreshToken> noUserxxxxxxxxxxxx = refreshTokenRedisRepository.findById("no userxxxxxxxxxxxx");
        assertThat(noUserxxxxxxxxxxxx).isEmpty();
    }

}