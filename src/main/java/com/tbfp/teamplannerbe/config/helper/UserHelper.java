package com.tbfp.teamplannerbe.config.helper;

import com.tbfp.teamplannerbe.config.security.SecurityUtils;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserHelper {

    private final MemberRepository memberRepository;

    public Long getCurrentUserId() {
        return SecurityUtils.getCurrentUserId();
    }

    public Member getMember(){
        return memberRepository.findById(getCurrentUserId()).orElseThrow(()-> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));
    }


}