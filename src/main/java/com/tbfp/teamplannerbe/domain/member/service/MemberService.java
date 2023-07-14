package com.tbfp.teamplannerbe.domain.member.service;

import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberService {
    List<Member> members();

    Optional<Member> findMemberByUsername(String username);

    String renewAccessToken(String refreshToken);

    Map<String,List<String>> getEnums();

    void registerMember(MemberRequestDto.SignUpRequestDto signUpRequestDto);

    MemberResponseDto.CheckDuplicateResponseDto checkDuplicate(MemberRequestDto.CheckDuplicateRequestDto checkDuplicateRequestDto);

    MemberResponseDto.SignUpResponseDto buildSignUpResponse(MemberRequestDto.SignUpRequestDto signUpRequestDto);

    MemberResponseDto.EmailAddressResponseDto sendVerificationEmail(MemberRequestDto.EmailAddressRequestDto emailAddressRequestDto);

    MemberResponseDto.VerificationResponseDto verifyCode(MemberRequestDto.VerificationRequestDto verificationRequestDto);

    void deleteMember(String username);

    MemberResponseDto.ForgotUsernameResponseDto findForgotUsername(MemberRequestDto.ForgotUsernameRequestDto forgotUsernameRequestDto);

    MemberResponseDto.ForgotPasswordResponseDto findForgotPassword(MemberRequestDto.ForgotPasswordRequestDto forgotPasswordRequestDto);

}