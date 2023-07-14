package com.tbfp.teamplannerbe.domain.member.service.impl;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import com.tbfp.teamplannerbe.domain.auth.repository.RefreshTokenRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.*;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
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

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;

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
    public MemberResponseDto.SignUpResponseDto buildSignUpResponse(MemberRequestDto.SignUpRequestDto signUpRequestDto) {

        String username = signUpRequestDto.getUsername();
        //Id 중복
        if(findMemberByUsername(username).isPresent()){
            throw new ApplicationException(DUPLICATE_USERNAME);
        }
        registerMember(signUpRequestDto);

        return MemberResponseDto.SignUpResponseDto.builder().
                message("회원가입이 완료되었습니다.").
                build();
    }

    @Override
    @Transactional
    public void registerMember(MemberRequestDto.SignUpRequestDto signUpRequestDto){
        try {
            signUpRequestDto.setPassword(bCryptPasswordEncoder.encode(signUpRequestDto.getPassword()));
            Member member = signUpRequestDto.toMember();
            Profile profile = signUpRequestDto.toProfile(member);
            memberRepository.save(member);
            profileRepository.save(profile);
        } catch (Exception e){
            throw new ApplicationException(MEMBER_REGISTER_FAIL);
        }
    }

    @Override
    @Transactional
    public Map<String,List<String>> getEnums(){
        Map<String, List<String>> enumsMap = new HashMap<>();

        enumsMap.put("job", Arrays.stream(Job.values())
                .map(Job::name)
                .collect(Collectors.toList()));

        enumsMap.put("education", Arrays.stream(Education.values())
                .map(Education::name)
                .collect(Collectors.toList()));

        enumsMap.put("gender", Arrays.stream(Gender.values())
                .map(Gender::name)
                .collect(Collectors.toList()));

        return enumsMap;
    }
    @Override
    @Transactional
    public MemberResponseDto.CheckDuplicateResponseDto checkDuplicate(MemberRequestDto.CheckDuplicateRequestDto checkDuplicateRequestDto){
        String username = checkDuplicateRequestDto.getUsername();
        if(findMemberByUsername(username).isPresent()){
            throw new ApplicationException(DUPLICATE_USERNAME);
        }
        return MemberResponseDto.CheckDuplicateResponseDto.builder().
                message("사용 가능한 아이디입니다.").
                build();
    }

    private ConcurrentHashMap<String, Map<String,Object>> authenticateMap = new ConcurrentHashMap<>();

    @Override
    @Transactional
    public MemberResponseDto.EmailAddressResponseDto sendVerificationEmail(MemberRequestDto.EmailAddressRequestDto emailAddressRequestDto) {//인증번호 발급, 재발급

        String emailAddress = emailAddressRequestDto.getEmailAddress();
        VerifyPurpose verifyPurpose = emailAddressRequestDto.getVerifyPurpose();
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

            return MemberResponseDto.EmailAddressResponseDto.builder().
                    message("이메일로 인증번호 발송에 성공했습니다.").
                    build();
        } catch (Exception e) {
            throw new ApplicationException(MAIL_ERROR);
        }
    }

    public VerificationStatus getVerificationStatus(String emailAddress, String userInputCode, VerifyPurpose verifyPurpose){

        try{
            authenticateMap.containsKey(emailAddress);
        } catch (NullPointerException e){
            return VerificationStatus.UNPROVIDED;
        }

        Map<String,Object> mapInfo = authenticateMap.get(emailAddress);

        if(mapInfo==null){
            return VerificationStatus.UNPROVIDED;
        }
        String verificationCode = mapInfo.get("verificationCode").toString();
        LocalDateTime expireDateTime = (LocalDateTime) mapInfo.get("expireDateTime");
        VerifyPurpose verifyPurposeInMap = (VerifyPurpose) mapInfo.get("verifyPurpose");

        //다른 타입의 인증번호
        if(!verifyPurposeInMap.equals(verifyPurpose)){
            sendVerificationEmail(
                    MemberRequestDto.EmailAddressRequestDto.builder().
                            emailAddress(emailAddress).
                            verifyPurpose(verifyPurpose).
                            build()
            );
            return VerificationStatus.UNPROVIDED;
        }
        //인증번호 기한 만료
        if (expireDateTime.isBefore(LocalDateTime.now())){//인증번호 만료시
            authenticateMap.remove(emailAddress);
            //인증번호 재발급
            sendVerificationEmail(
                    MemberRequestDto.EmailAddressRequestDto.builder().
                            emailAddress(emailAddress).
                            verifyPurpose(verifyPurpose).
                            build()
            );
            return VerificationStatus.EXPIRED;
        }
        //인증번호 일치
        if (userInputCode.equals(verificationCode)) return VerificationStatus.MATCHED;

        //인증번호 불일치
        return VerificationStatus.UNMATCHED;
    }

    @Override
    @Transactional
    public MemberResponseDto.VerificationResponseDto verifyCode(MemberRequestDto.VerificationRequestDto verificationRequestDto){
        VerificationStatus verificationStatus = getVerificationStatus(
                verificationRequestDto.getEmailAddress(),
                verificationRequestDto.getCode(),
                verificationRequestDto.getVerifyPurpose()
        );

        switch (verificationStatus) {
            case MATCHED:
                return MemberResponseDto.VerificationResponseDto.builder().
                        message("이메일 인증에 성공했습니다.").
                        build();
            case UNMATCHED:
                throw new ApplicationException(VERIFICATION_CODE_UNMATCHED);
            case EXPIRED:
                throw new ApplicationException(VERIFICATION_CODE_EXPIRED);
            case UNPROVIDED:
                throw new ApplicationException(VERIFICATION_CODE_UNPROVIDED);
            default:
                throw new RuntimeException("이메일 인증에 예기치 못한 오류가 발생했습니다.");
        }
    }

    @Override
    @Transactional
    public void deleteMember(String username){
        if(!findMemberByUsername(username).isPresent()){
            throw new ApplicationException(USER_NOT_FOUND);
        }
        memberRepository.updateMemberStateFalseByUsername(username);
    }

    @Override
    @Transactional
    public MemberResponseDto.ForgotUsernameResponseDto findForgotUsername(MemberRequestDto.ForgotUsernameRequestDto forgotUsernameRequestDto){
        String emailAddress = forgotUsernameRequestDto.getEmailAddress();
        String code = forgotUsernameRequestDto.getCode();
        VerificationStatus verificationStatus = getVerificationStatus(emailAddress,code,VerifyPurpose.FORGOT_ID);

        Boolean emailChecked = true;
        if(verificationStatus!=VerificationStatus.MATCHED) emailChecked = false;

        if(!emailChecked) throw new ApplicationException(UNVERIFIED_EMAIL);

        List<String> usernameList = memberRepository.findUsernamesByEmail(emailAddress).orElse(Collections.emptyList());
        if(usernameList.isEmpty()) throw new ApplicationException(USER_NOT_FOUND);

        return MemberResponseDto.ForgotUsernameResponseDto.builder().
                usernames(usernameList).
                message("아이디 조회에 성공했습니다.").
                build();
    }

    @Override
    @Transactional
    public MemberResponseDto.ForgotPasswordResponseDto findForgotPassword(MemberRequestDto.ForgotPasswordRequestDto forgotPasswordRequestDto) {
        String username = forgotPasswordRequestDto.getUsername();
        String emailAddress = forgotPasswordRequestDto.getEmailAddress();
        String code = forgotPasswordRequestDto.getCode();
        VerificationStatus verificationStatus = getVerificationStatus(emailAddress,code,VerifyPurpose.FORGOT_PASSWORD);

        Boolean emailChecked = true;
        if(verificationStatus!=VerificationStatus.MATCHED) emailChecked = false;

        if (!emailChecked) throw new ApplicationException(UNVERIFIED_EMAIL);

        if(!memberRepository.findUsernamesByEmail(emailAddress).orElse(Collections.emptyList()).contains(username)){
            throw new ApplicationException(INVALID_CONTACT_EMAIL);
        }

        Optional<Member> optionalMember = memberRepository.findMemberByUsername(username);
        if(!optionalMember.isPresent()) throw new ApplicationException(USER_NOT_FOUND);

        //임시 비밀번호 전송
        Member member = optionalMember.get();
        try {
            String newPassword = mailSenderService.getRandomPassword();
            String mailSubject = "TeamPlanner 임시 발급된 비밀번호입니다.";
            String mailBody = mailSubject + "\n반드시 접속 후 비밀번호를 재설정 하시길 바랍니다.\n" + newPassword.toString();
            mailSenderService.sendEmail(emailAddress, mailSubject, mailBody);

            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
            memberRepository.updateMemberPassword(member, encodedPassword);

            return MemberResponseDto.ForgotPasswordResponseDto.builder().
                    message("임시 비밀번호를 이메일로 보냈습니다. 반드시 접속 후 재설정해주세요").
                    build();

        } catch (Exception e) {
            throw new ApplicationException(MAIL_ERROR);
        }
    }
}