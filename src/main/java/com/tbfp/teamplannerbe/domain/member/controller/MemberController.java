package com.tbfp.teamplannerbe.domain.member.controller;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.cookie.CookieUtil;
import com.tbfp.teamplannerbe.domain.member.VerificationStatus;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.EmailAddressDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.CheckDuplicateRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.VerificationRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.SignUpRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.MemberRenewAccessTokenRequestDto;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "MemberController", description = "멤버 API")
public class MemberController {

    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    @GetMapping("/info")
    public ResponseEntity info(Principal principal) {
        return ResponseEntity.ok(principal);
    }


    @PostMapping("/renew-access-token")
    @Operation(summary = "액세스토큰 재발급 API", description = "리프레시토큰으로 액세스토큰 재발급한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity renewAccessToken(@RequestBody MemberRenewAccessTokenRequestDto memberRenewAccessTokenRequestDto, HttpServletResponse response) {
        log.info("memberRenewAccessTokenRequestDto.getRefreshToken() = " + memberRenewAccessTokenRequestDto.getRefreshToken());
        String accessToken = memberService.renewAccessToken(memberRenewAccessTokenRequestDto.getRefreshToken());
        CookieUtil.addCookie(response, "accessToken", accessToken, jwtProvider.ACCESS_TOKEN_EXPIRATION_TIME);

        // token body comment
//        return ResponseEntity.ok(MemberRenewAccessTokenResponseDto.builder()
//                                                                .accessToken(
//                                                                        accessToken
//        ).build()
//        );

        return ResponseEntity.ok("");
    }

    @PostMapping("/signup")
    public ResponseEntity<String> registerMember(@RequestBody SignUpRequestDto signUpRequestDto) {
        // 회원 가입 서비스 호출
        String loginId = signUpRequestDto.getLoginId();
        if(memberService.isDuplicate(loginId)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }

        memberService.registerMember(signUpRequestDto);
        return ResponseEntity.ok("회원 가입이 완료되었습니다.");
    }

    @PostMapping("/signup/is-duplicate")
    public ResponseEntity<String> checkDuplicate(@RequestBody CheckDuplicateRequestDto checkDuplicateRequestDto){
        String loginId = checkDuplicateRequestDto.getLoginId();
        if(memberService.isDuplicate(loginId)){
            return ResponseEntity.status(HttpStatus.CONFLICT).body("이미 존재하는 아이디입니다.");
        }
        return ResponseEntity.ok("생성 가능한 아이디입니다.");
    }

    @PostMapping("/signup/send-verification")
    public ResponseEntity<String> sendVerificationEmail(@RequestBody EmailAddressDto emailAddressToSendDto) {
        try {
            memberService.sendVerificationEmail(emailAddressToSendDto.getEmailAddress());
            return ResponseEntity.ok("이메일 전송에 성공했습니다.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("이메일 전송 실패");
        }
    }

    @PostMapping("/signup/verify")
    public ResponseEntity<String> verifyCode(@RequestBody VerificationRequestDto verificationRequestDto){
        try {
            // 인증번호 검증 로직 수행
            String emailAddress = verificationRequestDto.getEmailAddress();
            String code = verificationRequestDto.getCode().toString();
            VerificationStatus verificationStatus = memberService.verifyCode(emailAddress,code);

            switch (verificationStatus) {
                case MATCHED:
                    return ResponseEntity.ok("이메일 인증에 성공했습니다.");
                case UNMATCHED:
                    return ResponseEntity.status(400).body("이메일 인증에 실패했습니다 : Verification code is unmatched");
                case EXPIRED:
                    return ResponseEntity.status(400).body("이메일 인증에 실패했습니다 : Verification code is expired");
                case UNPROVIDED:
                    return ResponseEntity.status(400).body("이메일 인증에 실패했습니다 : Verification code is unprovided");
                default:
                    return ResponseEntity.status(400).body("Unexpected Error");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("인증 처리 실패");
        }
    }
}
