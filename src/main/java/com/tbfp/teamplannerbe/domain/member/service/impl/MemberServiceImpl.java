package com.tbfp.teamplannerbe.domain.member.service.impl;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import com.tbfp.teamplannerbe.domain.auth.repository.RefreshTokenRepository;
import com.tbfp.teamplannerbe.domain.auth.service.RefreshTokenService;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;

    @Override
    public Optional<Member> findMemberByLoginId(String loginId) {
        return memberRepository.findMemberByLoginId(loginId);
    }

    @Override
    @Transactional
    public List<Member> members() {
        return memberRepository.basicSelect();
    }


    @Override
    @Transactional
    public String renewAccessToken(String refreshToken) {
        log.info("MemberServiceImpl.renewAccessToken");
        log.info("refreshToken = " + refreshToken);

        // check refreshToken is valid ( expired?..)
        if (!jwtProvider.verifyToken(refreshToken)) {
            throw new ApplicationException(ApplicationErrorType.INVALID_REFRESH_TOKEN);
            // should re-login
        }

        // get user
        String loginId = jwtProvider.getLoginIdFromToken(refreshToken);
        RefreshToken refreshTokenFound = refreshTokenRepository.findRefreshTokenByMember_LoginId(loginId).orElseThrow(() -> new RuntimeException("no refresh token"));
        if (!refreshTokenFound.getToken().equals(refreshToken)) {
            throw new RuntimeException("not matching refreshToken");
        }

        return jwtProvider.generateAccessToken(loginId);
    }
}
