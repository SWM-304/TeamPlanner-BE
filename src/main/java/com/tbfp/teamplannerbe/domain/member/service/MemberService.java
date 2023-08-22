package com.tbfp.teamplannerbe.domain.member.service;

import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.RecruitmentApplicantResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface MemberService {
    List<Member> members();

    Optional<Member> findMemberByUsername(String username);

    String renewAccessToken(String refreshToken);

    Map<String, List<Map<String, String>>> getEnums();

    void registerMember(MemberRequestDto.SignUpRequestDto signUpRequestDto);

    MemberResponseDto.CheckDuplicateUsernameResponseDto checkDuplicateUsername(MemberRequestDto.CheckDuplicateUsernameRequestDto checkDuplicateUsernameRequestDto);

    MemberResponseDto.CheckDuplicateNicknameResponseDto checkDuplicateNickname(MemberRequestDto.CheckDuplicateNicknameRequestDto checkDuplicateNicknameRequestDto);

    MemberResponseDto.SignUpResponseDto buildSignUpResponse(MemberRequestDto.SignUpRequestDto signUpRequestDto);

    MemberResponseDto.EmailResponseDto sendVerificationEmail(MemberRequestDto.EmailRequestDto emailRequestDto);

    MemberResponseDto.VerificationResponseDto verifyCode(MemberRequestDto.VerificationRequestDto verificationRequestDto);

    void deleteMember(String username);

    MemberResponseDto.ForgotUsernameResponseDto findForgotUsername(MemberRequestDto.ForgotUsernameRequestDto forgotUsernameRequestDto);

    MemberResponseDto.ForgotPasswordResponseDto findForgotPassword(MemberRequestDto.ForgotPasswordRequestDto forgotPasswordRequestDto);

    Member findMemberByUsernameOrElseThrowApplicationException(String username);


    List<RecruitmentApplicantResponseDto> findApplicantList(String name);

    MemberResponseDto.getMemberInfoDto getMemberInfo(String username);

    MemberResponseDto.idAndEmailVerifyResponseDto verifyIdAndEmail(MemberRequestDto.IdAndEmailVerifyRequestDto verifyRequestDto);

    MemberResponseDto.NickNameAndEmailVerifyResponseDto verifyNicknameAndEmail(MemberRequestDto.NickNameAndEmailVerifyRequestDto verifyRequestDto);
}
