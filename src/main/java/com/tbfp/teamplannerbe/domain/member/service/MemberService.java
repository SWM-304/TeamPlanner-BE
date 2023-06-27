package com.tbfp.teamplannerbe.domain.member.service;

import com.tbfp.teamplannerbe.domain.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> members();

    Optional<Member> findMemberByLoginId(String loginId);


    String renewAccessToken(String refreshToken);
}
