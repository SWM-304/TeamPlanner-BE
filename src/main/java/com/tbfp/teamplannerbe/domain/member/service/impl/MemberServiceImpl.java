package com.tbfp.teamplannerbe.domain.member.service.impl;

import com.tbfp.teamplannerbe.config.redis.util.EmailRedisUtil;
import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.entity.RefreshToken;
import com.tbfp.teamplannerbe.domain.auth.repository.RefreshTokenRepository;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.*;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.RecruitmentApplicantResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.getMemberInfoDto;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.profile.entity.BasicProfile;
import com.tbfp.teamplannerbe.domain.profile.repository.BasicProfileRepository;
import com.tbfp.teamplannerbe.domain.member.repository.MemberRepository;
import com.tbfp.teamplannerbe.domain.member.service.MailSenderService;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import com.tbfp.teamplannerbe.domain.profile.service.ProfileService;
import com.tbfp.teamplannerbe.domain.recruitment.entity.Recruitment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static com.tbfp.teamplannerbe.domain.board.entity.QBoard.board;
import static com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType.*;
import static com.tbfp.teamplannerbe.domain.member.entity.QMember.member;
import static com.tbfp.teamplannerbe.domain.recruitment.entity.QRecruitment.recruitment;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class MemberServiceImpl implements MemberService {

    private final MemberRepository memberRepository;
    private final BasicProfileRepository basicProfileRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtProvider jwtProvider;
    private final MailSenderService mailSenderService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ProfileService profileService;
    private final EmailRedisUtil emailRedisUtil;
    @Override
    public Optional<Member> findMemberByUsername(String username) {
        return memberRepository.findMemberByUsername(username);
    }

    @Override
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

        String nickname = signUpRequestDto.getNickname();
        //닉네임 중복
        if(memberRepository.findByNickname(nickname).isPresent()){
            throw new ApplicationException(DUPLICATE_NICKNAME);
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
            memberRepository.save(member);
            BasicProfile basicProfile = signUpRequestDto.toBasicProfile(member);
            basicProfileRepository.save(basicProfile);
        } catch (Exception e){
            throw new ApplicationException(MEMBER_REGISTER_FAIL);
        }
    }

    @Override
    @Transactional
    public Map<String, List<Map<String, String>>> getEnums(){
        Map<String, List<Map<String, String>>> enumsMap = new HashMap<>();

        enumsMap.put("job", Arrays.stream(Job.values())
                .map(job -> mapEntry(job.name(), job.getLabel()))
                .collect(Collectors.toList()));

        enumsMap.put("education", Arrays.stream(Education.values())
                .map(education -> mapEntry(education.name(), education.getLabel()))
                .collect(Collectors.toList()));

        enumsMap.put("gender", Arrays.stream(Gender.values())
                .map(gender -> mapEntry(gender.name(), gender.getLabel()))
                .collect(Collectors.toList()));

        return enumsMap;
    }

    private static Map<String, String> mapEntry(String name, String label) {
        Map<String, String> entry = new HashMap<>();
        entry.put("name", name);
        entry.put("label", label);
        return entry;
    }

    @Override
    @Transactional
    public MemberResponseDto.CheckDuplicateUsernameResponseDto checkDuplicateUsername(MemberRequestDto.CheckDuplicateUsernameRequestDto checkDuplicateUsernameRequestDto){
        String username = checkDuplicateUsernameRequestDto.getUsername();
        if(findMemberByUsername(username).isPresent()){
            throw new ApplicationException(DUPLICATE_USERNAME);
        }
        return MemberResponseDto.CheckDuplicateUsernameResponseDto.builder().
                message("사용 가능한 아이디입니다.").
                build();
    }


    @Override
    @Transactional
    public MemberResponseDto.CheckDuplicateNicknameResponseDto checkDuplicateNickname(MemberRequestDto.CheckDuplicateNicknameRequestDto checkDuplicateNicknameRequestDto){
        String nickname = checkDuplicateNicknameRequestDto.getNickname();
        if(memberRepository.findByNickname(nickname).isPresent()){
            throw new ApplicationException(DUPLICATE_NICKNAME);
        }
        return MemberResponseDto.CheckDuplicateNicknameResponseDto.builder().
                message("사용 가능한 닉네임입니다.").
                build();
    }


//    private ConcurrentHashMap<String, Map<String,Object>> authenticateMap = new ConcurrentHashMap<>();


    @Override
    @Transactional
    public MemberResponseDto.EmailResponseDto sendVerificationEmail(MemberRequestDto.EmailRequestDto emailRequestDto) {//인증번호 발급, 재발급

        String email = emailRequestDto.getEmail();
        VerifyPurpose verifyPurpose = emailRequestDto.getVerifyPurpose();
        if (emailRedisUtil.existData(email)) emailRedisUtil.deleteData(email);


        try {



            Integer verificationCode = mailSenderService.getVerificationNumber();
            String mailSubject = "TeamPlanner " + verifyPurpose.getLabel() + " 인증번호입니다.";
            String mailBody = mailSubject + "\n" + verificationCode.toString();
            mailSenderService.sendEmail(email, mailSubject, mailBody);

            //Concurrent에 이메일주소, 인증번호, 만료시간 저장
//            LocalDateTime expireDateTime = LocalDateTime.now().plusSeconds(180);
//            Map<String,Object> innerMap = new ConcurrentHashMap<>();
//            innerMap.put("verificationCode",verificationCode);
//            innerMap.put("expireDateTime",expireDateTime);
//            innerMap.put("verifyPurpose",verifyPurpose);
            emailRedisUtil.setListData(email,verificationCode,verifyPurpose,60*3L);
//            authenticateMap.put(emailAddress,innerMap);


            return MemberResponseDto.EmailResponseDto.builder().
                    message("이메일로 인증번호 발송에 성공했습니다.").
                    build();
        } catch (Exception e) {
            throw new ApplicationException(MAIL_ERROR);
        }
    }

    public VerificationStatus getVerificationStatus(String email, String userInputCode, VerifyPurpose verifyPurpose){




        if(!emailRedisUtil.existData(email))  return VerificationStatus.UNPROVIDED;;


        List<String> info = emailRedisUtil.getData(email);
        if(info.isEmpty() || info.get(0)==null){
            return VerificationStatus.UNPROVIDED;
        }

        String[] splitInfo = info.get(0).split("\\|");
        String verificationCode = splitInfo[0];
        VerifyPurpose verifyPurposeInMap = VerifyPurpose.valueOf(splitInfo[1]);

        //다른 타입의 인증번호
        if(!verifyPurposeInMap.equals(verifyPurpose)){
            sendVerificationEmail(
                    MemberRequestDto.EmailRequestDto.builder().
                            email(email).
                            verifyPurpose(verifyPurpose).
                            build()
            );
            return VerificationStatus.UNPROVIDED;
        }
        //인증번호 기한 만료

        //redis 는 만료시간되면 자동으로 삭제됨
//        if (expireDateTime.isBefore(LocalDateTime.now())){//인증번호 만료시
//            authenticateMap.remove(emailAddress);
//            //인증번호 재발급
//            sendVerificationEmail(
//                    MemberRequestDto.EmailAddressRequestDto.builder().
//                            emailAddress(emailAddress).
//                            verifyPurpose(verifyPurpose).
//                            build()
//            );
//            return VerificationStatus.EXPIRED;
//        }
        //인증번호 일치
        if (userInputCode.equals(verificationCode)){
            emailRedisUtil.deleteData(email);
            return VerificationStatus.MATCHED;
        }
        //인증번호 불일치
        return VerificationStatus.UNMATCHED;
    }

    @Override
    @Transactional
    public MemberResponseDto.VerificationResponseDto verifyCode(MemberRequestDto.VerificationRequestDto verificationRequestDto){
        VerificationStatus verificationStatus = getVerificationStatus(
                verificationRequestDto.getEmail(),
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
        String email = forgotUsernameRequestDto.getEmail();
//        String code = forgotUsernameRequestDto.getCode();
//        VerificationStatus verificationStatus = getVerificationStatus(email,code,VerifyPurpose.FORGOT_ID);
//
//        Boolean emailChecked = true;
//        if(verificationStatus!=VerificationStatus.MATCHED) emailChecked = false;
//
//        if(!emailChecked) throw new ApplicationException(UNVERIFIED_EMAIL);

        List<String> usernameList = memberRepository.findUsernamesByEmail(forgotUsernameRequestDto.getEmail()).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        if(usernameList.isEmpty()) throw new ApplicationException(USER_NOT_FOUND);

        try {
            String mailSubject = "TeamPlanner 아이디 안내: " + forgotUsernameRequestDto.getEmail();

            StringBuilder mailBodyBuilder = new StringBuilder();
            mailBodyBuilder.append("<html><body style=\"font-family: Arial, sans-serif;\">");
            mailBodyBuilder.append("<div style=\"background-color: #f4f4f4; padding: 20px;\">");
            mailBodyBuilder.append("<h2 style=\"color: #333;\">안녕하세요, TeamPlanner 계정 관련 안내 메일입니다.</h2>");
            mailBodyBuilder.append("<p style=\"color: #555;\">요청하신 이메일(").append(forgotUsernameRequestDto.getEmail()).append(")에 연동된 아이디는 다음과 같습니다:</p>");
            mailBodyBuilder.append("<ul>");
            for (String username : usernameList) {
                mailBodyBuilder.append("<li style=\"color: #777;\">").append(username).append("</li>");
            }
            mailBodyBuilder.append("</ul>");
            mailBodyBuilder.append("<p style=\"color: #555;\">더 궁금한 사항이 있으시면 언제든지 문의해주세요.</p>");
            mailBodyBuilder.append("<p style=\"color: #555;\">감사합니다,<br>TeamPlanner 드림</p>");
            mailBodyBuilder.append("</div>");
            mailBodyBuilder.append("</body></html>");

            String mailBody = mailBodyBuilder.toString();
            mailSenderService.sendEmail(email, mailSubject, mailBody);
        }catch (Exception e) {
            throw new ApplicationException(MAIL_ERROR);
        }


        return MemberResponseDto.ForgotUsernameResponseDto.builder().
                usernames(usernameList).
                message("아이디 조회에 성공했습니다.").
                build();
    }

    @Override
    @Transactional
    public MemberResponseDto.ForgotPasswordResponseDto findForgotPassword(MemberRequestDto.ForgotPasswordRequestDto forgotPasswordRequestDto) {
        String username = forgotPasswordRequestDto.getUsername();
        String email = forgotPasswordRequestDto.getEmail();
        String code = forgotPasswordRequestDto.getCode();
        VerificationStatus verificationStatus = getVerificationStatus(email,code,VerifyPurpose.FORGOT_PASSWORD);

        Boolean emailChecked = true;
        if(verificationStatus!=VerificationStatus.MATCHED) emailChecked = false;

        if (!emailChecked) throw new ApplicationException(UNVERIFIED_EMAIL);

        if(!memberRepository.findUsernamesByEmail(email).orElse(Collections.emptyList()).contains(username)){
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
            mailSenderService.sendEmail(email, mailSubject, mailBody);

            String encodedPassword = bCryptPasswordEncoder.encode(newPassword);
            memberRepository.updateMemberPassword(member, encodedPassword);

            return MemberResponseDto.ForgotPasswordResponseDto.builder().
                    message("임시 비밀번호를 이메일로 보냈습니다. 반드시 접속 후 재설정해주세요").
                    build();

        } catch (Exception e) {
            throw new ApplicationException(MAIL_ERROR);
        }
    }

    @Override
    public Member findMemberByUsernameOrElseThrowApplicationException(String username) {
        return memberRepository.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
    }

    @Override
    public List<RecruitmentApplicantResponseDto> findApplicantList(String username) {
        List<Recruitment> applicantList = memberRepository.getApplicantList(username);


        List<RecruitmentApplicantResponseDto> result = applicantList.stream().map(i -> new RecruitmentApplicantResponseDto(i)).collect(Collectors.toList());
        return result;
    }

    @Override
    public getMemberInfoDto getMemberInfo(String username) {
        BasicProfile basicProfile = profileService.getBasicProfile(username);
        Member member = memberRepository.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
        return getMemberInfoDto.builder()
                .username(username)
                .nickname(member.getNickname())
                .memberId(member.getId())
                .profileImg(basicProfile.getProfileImage())
                .build();
    }

    @Override
    public MemberResponseDto.idAndEmailVerifyResponseDto verifyIdAndEmail(MemberRequestDto.IdAndEmailVerifyRequestDto verifyRequestDto) {

        memberRepository.findByUsernameAndEmail(verifyRequestDto.getUsername(),verifyRequestDto.getEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));


        return MemberResponseDto.idAndEmailVerifyResponseDto.builder()
                .message("사용자 정보가 일치합니다")
                .build();
    }

    @Override
    public MemberResponseDto.NickNameAndEmailVerifyResponseDto verifyNicknameAndEmail(MemberRequestDto.NickNameAndEmailVerifyRequestDto verifyRequestDto) {

        memberRepository.findByNicknameAndEmail(verifyRequestDto.getNickname(),verifyRequestDto.getEmail())
                .orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));


        return MemberResponseDto.NickNameAndEmailVerifyResponseDto.builder()
                .message("사용자 정보가 일치합니다")
                .build();

    }
    @Override
    public Member findMemberByNicknameOrElseThrowApplicationException(String nickname) {
        return memberRepository.findByNickname(nickname).orElseThrow(() -> new ApplicationException(USER_NOT_FOUND));
    }
}