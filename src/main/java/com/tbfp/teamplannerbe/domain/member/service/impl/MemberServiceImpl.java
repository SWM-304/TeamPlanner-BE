package com.tbfp.teamplannerbe.domain.member.service.impl;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import com.tbfp.teamplannerbe.domain.auth.repository.RefreshTokenRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.ErrorCode;
import com.tbfp.teamplannerbe.domain.member.VerificationStatus;
import com.tbfp.teamplannerbe.domain.member.VerifyPurpose;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.SignUpRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.ForgotPasswordResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.EmailAddressResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.ForgotUsernameResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.SignUpResponseDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.entity.Profile;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.repository.ProfileRepository;
import com.tbfp.teamplannerbe.domain.member.service.MailSenderService;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.REFRESH_TOKEN_FOR_USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final ProfileRepository profileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final MailSenderService mailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public Optional<Member> findMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username);
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
        String username = jwtProvider.getUsernameFromToken(refreshToken);
        RefreshToken refreshTokenFound = refreshTokenRepository.findById(username).orElseThrow(() -> new ApplicationException(REFRESH_TOKEN_FOR_USER_NOT_FOUND));
        if (!refreshTokenFound.getToken().equals(refreshToken)) {
            throw new RuntimeException("not matching refreshToken");
        }

        return jwtProvider.generateAccessToken(username);
    }

    @Override
    @Transactional
    public void registerMember(SignUpRequestDto signUpRequestDto){
        try {
            signUpRequestDto.setPassword(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()));
            Member member = signUpRequestDto.toMember();
            Profile profile = signUpRequestDto.toProfile(member);
            memberRepository.save(member);
            profileRepository.save(profile);
        } catch (Exception e){
            throw new IllegalArgumentException("회원 생성에 실패했습니다.");
        }
    }

    @Override
    @Transactional
    public boolean isDuplicate(String username){
        return findMemberByUsername(username).isPresent();
    }




    @Override
    @Transactional
    public SignUpResponseDto buildSignUpResponse(String username, Errors errors) {

        List<String> errorMessages = new ArrayList<>();
        List<ErrorCode> errorCodes = new ArrayList<>();

        //Id 중복
        if(isDuplicate(username)){
            errorCodes.add(ErrorCode.DUPLICATE_USERNAME);
            return SignUpResponseDto.builder().
                    success(false).
                    messages(Collections.singletonList("이미 존재하는 아이디입니다.")).
                    errorCodes(errorCodes).
                    build();
        }

        //Validation error
        if (errors.hasErrors()) {
            List<FieldError> fieldErrors = errors.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                errorMessages.add(fieldError.getDefaultMessage());
                ErrorCode errorCode = ErrorCode.mapToErrorCode(fieldError.getField());
                errorCodes.add(errorCode);
            }

            return SignUpResponseDto.builder().
                    success(false).
                    messages(errorMessages).
                    errorCodes(errorCodes).
                    build();
        }

        return SignUpResponseDto.builder().
                success(true).
                messages(Collections.singletonList("회원가입이 완료되었습니다.")).
                errorCodes(null).
                build();
    }

    private ConcurrentHashMap<String, Map<String,Object>> authenticateMap = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public EmailAddressResponseDto sendVerificationEmail(String emailAddress, VerifyPurpose verifyPurpose, Errors errors) {//인증번호 발급, 재발급

        List<String> errorMessages = new ArrayList<>();
        List<ErrorCode> errorCodes = new ArrayList<>();

        //Validation error
        if (errors.hasErrors()) {
            List<FieldError> fieldErrors = errors.getFieldErrors();

            for(FieldError fieldError : fieldErrors){
                errorMessages.add(fieldError.getDefaultMessage());
                ErrorCode errorCode = ErrorCode.mapToErrorCode(fieldError.getField());
                errorCodes.add(errorCode);
            }

            return EmailAddressResponseDto.builder().
                    success(false).
                    messages(errorMessages).
                    errorCodes(errorCodes).
                    build();
        }

        try {
            if (authenticateMap.containsKey(emailAddress)) authenticateMap.remove(emailAddress);

            Integer verificationCode = mailSenderService.getVerificationNumber();
            String mailSubject = "TeamPlanner " + verifyPurpose.getDisplayName() + " 인증번호입니다.";
            String mailBody = mailSubject + "\n" + verificationCode.toString();
            mailSenderService.sendEmail(emailAddress, mailSubject, mailBody);

            //Concurrent에 이메일주소, 인증번호, 만료시간 저장
            LocalDateTime expireDateTime = LocalDateTime.now().plusSeconds(180);
            Map<String,Object> innerMap = new ConcurrentHashMap<>();
            innerMap.put("verificationCode",verificationCode);
            innerMap.put("expireDateTime",expireDateTime);
            innerMap.put("verifyPurpose",verifyPurpose);
            authenticateMap.put(emailAddress,innerMap);

            return EmailAddressResponseDto.builder().
                    success(true).
                    messages(null).
                    errorCodes(null).
                    build();
        } catch (Exception e) {
            return EmailAddressResponseDto.builder().
                    success(false).
                    messages(Collections.singletonList("전송 중 오류가 발생했습니다.")).
                    errorCodes(Collections.singletonList(ErrorCode.INVALID_DEFAULT))
                    .build();
        }
    }

    @Override
    @Transactional
    public VerificationStatus verifyCode(String emailAddress, String userInputCode, VerifyPurpose verifyPurpose){
        try{
            authenticateMap.containsKey(emailAddress);
        } catch (NullPointerException e){
            return VerificationStatus.UNPROVIDED;
        }

        Map<String,Object> mapInfo = authenticateMap.get(emailAddress);

        String verificationCode = mapInfo.get("verificationCode").toString();
        LocalDateTime expireDateTime = (LocalDateTime) mapInfo.get("expireDateTime");
        VerifyPurpose verifyPurposeInMap = (VerifyPurpose) mapInfo.get("verifyPurpose");

        //다른 타입의 인증번호
        if(!verifyPurposeInMap.equals(verifyPurpose)){
            return VerificationStatus.UNPROVIDED;
        }
        //인증번호 기한 만료
        if (expireDateTime.isBefore(LocalDateTime.now())){//인증번호 만료시
            authenticateMap.remove(emailAddress);
            //인증번호 재발급
            sendVerificationEmail(emailAddress,verifyPurpose,null);
            return VerificationStatus.EXPIRED;
        }
        //인증번호 일치
        if (userInputCode.equals(verificationCode)) return VerificationStatus.MATCHED;

        //인증번호 불일치
        return VerificationStatus.UNMATCHED;
    }

    @Override
    @Transactional
    public boolean deleteMember(String username){
        if(isDuplicate(username)){
            memberRepository.updateMemberStateFalseByUsername(username);
            return true;
        }
        return false;
    }

    @Override
    @Transactional
    public ForgotUsernameResponseDto findForgotUsername(String emailAddress, Boolean emailChecked){
        if(!emailChecked){
            return ForgotUsernameResponseDto.builder().
                    success(false).
                    usernames(null).
                    messages(Collections.singletonList("이메일 인증이 완료되지 않았습니다.")).
                    errorCodes(Collections.singletonList(ErrorCode.UNVERIFIED)).
                    build();
        }

        List<String> usernameList = memberRepository.findUsernamesByEmail(emailAddress).orElse(Collections.emptyList());
        if(usernameList.isEmpty()){
            //그 멤버에 대해 id 주기
            return ForgotUsernameResponseDto.builder().
                    success(false).
                    usernames(null).
                    messages(Collections.singletonList("가입된 아이디가 없습니다.")).
                    errorCodes(Collections.singletonList(ErrorCode.USERNAME_ABSENT)).
                    build();
        }
        return ForgotUsernameResponseDto.builder().
                        success(true).
                        usernames(usernameList).
                        messages(Collections.singletonList("아이디 조회에 성공했습니다.")).
                        errorCodes(null).
                        build();
    }

    @Override
    @Transactional
    public ForgotPasswordResponseDto findForgotPassword(String username, String emailAddress, Boolean emailChecked) {
        if (!emailChecked) {
            return ForgotPasswordResponseDto.builder().
                    success(false).
                    messages(Collections.singletonList("이메일 인증이 완료되지 않았습니다.")).
                    errorCodes(Collections.singletonList(ErrorCode.UNVERIFIED)).
                    build();
        }

        if(!memberRepository.findUsernamesByEmail(emailAddress).orElse(Collections.emptyList()).contains(username)){
            return ForgotPasswordResponseDto.builder().
                    success(false).
                    messages(Collections.singletonList("입력하신 아이디에 등록된 이메일이 아닙니다.")).
                    errorCodes(Collections.singletonList(ErrorCode.INVALID_CONTACTEMAIL)).
                    build();
        }

        Optional<Member> optionalMember = memberRepository.findMemberByUsername(username);
        if(!optionalMember.isPresent()){
            return ForgotPasswordResponseDto.builder().
                    success(false).
                    messages(Collections.singletonList("가입된 아이디가 없습니다.")).
                    errorCodes(Collections.singletonList(ErrorCode.USERNAME_ABSENT)).
                    build();
        }
        Member member = optionalMember.get();


        //임시 비밀번호 전송
        try {
            String newPassword = mailSenderService.getRandomPassword();
            String mailSubject = "TeamPlanner 임시 발급된 비밀번호입니다.";
            String mailBody = mailSubject + "\n반드시 접속 후 비밀번호를 재설정 하시길 바랍니다.\n" + newPassword.toString();
            mailSenderService.sendEmail(emailAddress, mailSubject, mailBody);

            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
            memberRepository.updateMemberPassword(member, encodedPassword);

            return ForgotPasswordResponseDto.builder().
                    success(true).
                    messages(Collections.singletonList("임시 비밀번호를 이메일로 보냈습니다. 반드시 접속 후 재설정해주세요")).
                    errorCodes(null).
                    build();

        } catch (Exception e) {
            return ForgotPasswordResponseDto.builder().
                    success(false).
                    messages(Collections.singletonList("임시 비밀번호 전송 중 오류가 발생했습니다.")).
                    errorCodes(Collections.singletonList(ErrorCode.INVALID_DEFAULT))
                    .build();
        }
    }
}
