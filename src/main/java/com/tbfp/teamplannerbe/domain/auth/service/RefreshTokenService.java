package com.tbfp.teamplannerbe.domain.auth.service;

import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import com.tbfp.teamplannerbe.domain.auth.repository.RefreshTokenRepository;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void setRefreshToken(String loginId, String refreshToken) {
        Optional<RefreshToken> refreshTokenByMemberLoginId = refreshTokenRepository.findRefreshTokenByMember_LoginId(loginId);
        if (refreshTokenByMemberLoginId.isPresent()) {
            refreshTokenByMemberLoginId.get().updateToken(refreshToken);
        } else {
            Member member = memberRepository.findMemberByLoginId(loginId).orElseThrow(() -> new RuntimeException("no member by loginid"));
            refreshTokenRepository.save(RefreshToken.builder().token(refreshToken).member(member).build());
        }
    }
}
