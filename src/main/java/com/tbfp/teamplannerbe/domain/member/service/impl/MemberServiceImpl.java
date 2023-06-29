package com.tbfp.teamplannerbe.domain.member.service.impl;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import com.tbfp.teamplannerbe.domain.auth.repository.RefreshTokenRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.VerificationStatus;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.SignUpRequestDto;
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
import java.util.Hashtable;
import java.util.List;
import java.util.Optional;

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
    public Optional<Member> findMemberByLoginId(String loginId) {
        return memberRepository.findMemberByLoginId(loginId);
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
        String loginId = jwtProvider.getLoginIdFromToken(refreshToken);
        RefreshToken refreshTokenFound = refreshTokenRepository.findById(loginId).orElseThrow(() -> new ApplicationException(REFRESH_TOKEN_FOR_USER_NOT_FOUND));
        if (!refreshTokenFound.getToken().equals(refreshToken)) {
            throw new RuntimeException("not matching refreshToken");
        }

        return jwtProvider.generateAccessToken(loginId);
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
    public boolean isDuplicate(String loginId){
        if(findMemberByLoginId(loginId).isPresent()) return true;
        return false;
    }

    private Hashtable<String, Hashtable<String,Object>> authenticateTable = new Hashtable<>();
    @Override
    @Transactional
    public void sendVerificationEmail(String emailAddress) {//인증번호 발급, 재발급
        try {
            if (authenticateTable.containsKey(emailAddress)) authenticateTable.remove(emailAddress);

            Integer verificationCode = mailSenderService.getVerificationNumber();
            String mailBody = "TeamPlanner 회원가입 인증번호입니다.\n" + verificationCode.toString();
            mailSenderService.sendEmail(emailAddress, "TeamPlanner 회원가입 인증번호입니다.", mailBody);

            //hashtable에 이메일주소, 인증번호, 만료시간 저장
            LocalDateTime expireDateTime = LocalDateTime.now().plusSeconds(180);
            Hashtable<String,Object> innerHashTable = new Hashtable<>();
            innerHashTable.put("verificationCode",verificationCode);
            innerHashTable.put("expireDateTime",expireDateTime);
            authenticateTable.put(emailAddress,innerHashTable);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new IllegalArgumentException("이메일 전송 중 오류가 발생했습니다.", e);
        }
    }

    @Override
    @Transactional
    public VerificationStatus verifyCode(String emailAddress, String userInputCode){
        try{
            authenticateTable.containsKey(emailAddress);
        } catch (NullPointerException e){
            return VerificationStatus.UNPROVIDED;
        }

        String verificationCode = authenticateTable.get(emailAddress).get("verificationCode").toString();
        LocalDateTime expireDateTime = (LocalDateTime) authenticateTable.get(emailAddress).get("expireDateTime");

        //인증번호 기한 만료
        if (expireDateTime.isBefore(LocalDateTime.now())){//인증번호 만료시
            authenticateTable.remove(emailAddress);
            //인증번호 재발급
            sendVerificationEmail(emailAddress);
            return VerificationStatus.EXPIRED;
        }
        //인증번호 일치
        if (userInputCode.equals(verificationCode)) return VerificationStatus.MATCHED;

        //인증번호 불일치
        return VerificationStatus.UNMATCHED;
    }

}
