package com.tbfp.teamplannerbe.domain.auth.service;

import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import com.tbfp.teamplannerbe.domain.auth.repository.RefreshTokenRepository;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final MemberRepository memberRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public void setRefreshToken(String username, String refreshToken) {
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new RuntimeException("no member by username"));
        refreshTokenRepository.save(RefreshToken.builder().token(refreshToken).id(member.getUsername()).build());
    }
}
