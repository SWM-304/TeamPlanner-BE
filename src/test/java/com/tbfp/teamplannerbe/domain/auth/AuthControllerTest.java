package com.tbfp.teamplannerbe.domain.auth;

import com.tbfp.teamplannerbe.common.BaseControllerTest;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.MemberLoginRequestDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import javax.persistence.EntityManager;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.cookie;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthControllerTest extends BaseControllerTest {

    @Autowired
    EntityManager em;
    @Autowired
    JwtProvider jwtProvider;

    @BeforeEach
    public void beforeAll() {
        em.persist(Member.builder().username("member1").password(new BCryptPasswordEncoder().encode("1234")).state(true).build());
    }

    @Test
    public void usernamePasswordLogin() throws Exception {
        String username = "member1";
        String rawPassword = "1234";
        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto(username, rawPassword);

        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
        );

        resultActions.andExpect(status().isOk())
                .andExpect(result -> System.out.println("result = " + result))
                .andExpect(
                        result -> {
                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("accessToken").getValue())).isEqualTo(username);
                            assertThat(jwtProvider.getUsernameFromToken(result.getResponse().getCookie("refreshToken").getValue())).isEqualTo(username);
                        }
                );
    }

    @Test
    public void usernamePasswordLoginFail() throws Exception {
        // given
        String username = "member1";
        String rawPassword = "wrongPassword";
        MemberLoginRequestDto memberLoginRequestDto = new MemberLoginRequestDto(username, rawPassword);

        // when
        ResultActions resultActions = mockMvc.perform(
                post("/api/v1/member/login")
                        .content(objectMapper.writeValueAsString(memberLoginRequestDto))
        );

        // then
        resultActions.andExpect(status().isBadRequest())
                .andExpect(result -> System.out.println("result = " + result))
                .andExpect(
                        cookie().doesNotExist("accessToken")
                )
                .andExpect(
                        cookie().doesNotExist("refreshToken")
                );

    }
}