package com.tbfp.teamplannerbe.domain.member.service;

import com.tbfp.teamplannerbe.domain.member.VerificationStatus;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.SignUpRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.SignUpResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> members();

    Optional<Member> findMemberByLoginId(String loginId);

    String renewAccessToken(String refreshToken);

    void registerMember(SignUpRequestDto signUpRequestDto);

    boolean isDuplicate(String loginId);

    SignUpResponseDto buildSignUpResponse(String loginId, Errors errors);

    void sendVerificationEmail(String emailAddress);

    VerificationStatus verifyCode(String emailAddress, String userInputCode);

    boolean deleteMember(String loginId);
}
