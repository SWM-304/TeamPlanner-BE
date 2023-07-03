package com.tbfp.teamplannerbe.domain.member.service;

import com.tbfp.teamplannerbe.domain.member.VerificationStatus;
import com.tbfp.teamplannerbe.domain.member.VerifyPurpose;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.SignUpRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.ForgotPasswordResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.EmailAddressResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.ForgotUsernameResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.SignUpResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Optional;

public interface MemberService {
    List<Member> members();

    Optional<Member> findMemberByUsername(String username);

    String renewAccessToken(String refreshToken);

    void registerMember(SignUpRequestDto signUpRequestDto);

    boolean isDuplicate(String username);

    SignUpResponseDto buildSignUpResponse(String username, Errors errors);

    EmailAddressResponseDto sendVerificationEmail(String emailAddress, VerifyPurpose verifyPurpose, Errors errors);

    VerificationStatus verifyCode(String emailAddress, String userInputCode, VerifyPurpose verifyPurpose);

    boolean deleteMember(String username);

    ForgotUsernameResponseDto findForgotUsername(String emailAddress, Boolean emailChecked);

    ForgotPasswordResponseDto findForgotPassword(String username, String emailAddress, Boolean emailChecked);

}
