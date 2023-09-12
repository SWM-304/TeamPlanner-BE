package com.tbfp.teamplannerbe.domain.member.controller;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.cookie.CookieUtil;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.security.Principal;
import java.util.*;

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
        return ResponseEntity.ok(
                memberService.getMemberInfo(principal.getName())
        );
    }


    @PostMapping("/renew-access-token")
    @Operation(summary = "액세스토큰 재발급 API", description = "리프레시토큰으로 액세스토큰 재발급한다.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청"),
            @ApiResponse(responseCode = "500", description = "내부 서버 에러"),
    })
    public ResponseEntity renewAccessToken(@RequestBody MemberRequestDto.MemberRenewAccessTokenRequestDto memberRenewAccessTokenRequestDto, HttpServletResponse response) {
        log.info("memberRenewAccessTokenRequestDto.getRefreshToken() = " + memberRenewAccessTokenRequestDto.getRefreshToken());
        String accessToken = memberService.renewAccessToken(memberRenewAccessTokenRequestDto.getRefreshToken());
        CookieUtil.addCookie(response, "accessToken", accessToken, jwtProvider.ACCESS_TOKEN_EXPIRATION_TIME);

        // token body comment
        return ResponseEntity.ok(MemberResponseDto.MemberRenewAccessTokenResponseDto.builder()
                .accessToken(accessToken).build()
        );
    }

    @PostMapping("/signup")
    public ResponseEntity<MemberResponseDto.SignUpResponseDto> registerMember(@Validated @RequestBody MemberRequestDto.SignUpRequestDto signUpRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(memberService.buildSignUpResponse(signUpRequestDto));
    }

    @GetMapping("/signup/enums")
    public ResponseEntity<Map<String, List<Map<String, String>>>> getEnums() {
        return ResponseEntity.ok(memberService.getEnums());
    }

    @PostMapping("/signup/check-duplicate/username")
    public ResponseEntity<MemberResponseDto.CheckDuplicateUsernameResponseDto> checkDuplicateUsername(@Valid @RequestBody MemberRequestDto.CheckDuplicateUsernameRequestDto checkDuplicateUsernameRequestDto){
        return ResponseEntity.ok(memberService.checkDuplicateUsername(checkDuplicateUsernameRequestDto));
    }

    @PostMapping("/signup/check-duplicate/nickname")
    public ResponseEntity<MemberResponseDto.CheckDuplicateNicknameResponseDto> checkDuplicateNickname(@Valid @RequestBody MemberRequestDto.CheckDuplicateNicknameRequestDto checkDuplicateNicknameRequestDto){
        return ResponseEntity.ok(memberService.checkDuplicateNickname(checkDuplicateNicknameRequestDto));
    }

    @PostMapping("/signup/send-verification")
    public ResponseEntity<MemberResponseDto.EmailResponseDto> sendVerificationEmail(@Valid @RequestBody MemberRequestDto.EmailRequestDto emailRequestDto) {
        return ResponseEntity.ok().body(memberService.sendVerificationEmail(emailRequestDto));
    }

    @PostMapping("/signup/verify")
    public ResponseEntity<MemberResponseDto.VerificationResponseDto> verifyCode(@Valid @RequestBody MemberRequestDto.VerificationRequestDto verificationRequestDto){
        return ResponseEntity.ok().body(memberService.verifyCode(verificationRequestDto));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteMember(@PathVariable("username") String username){
        memberService.deleteMember(username);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/forgot-username")
    public ResponseEntity<MemberResponseDto.ForgotUsernameResponseDto> giveUsername(@RequestBody MemberRequestDto.ForgotUsernameRequestDto forgotUsernameRequestDto){
        return ResponseEntity.ok().body(memberService.findForgotUsername(forgotUsernameRequestDto));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<MemberResponseDto.ForgotPasswordResponseDto> givePassword(@RequestBody MemberRequestDto.ForgotPasswordRequestDto forgotPasswordRequestDto){
        return ResponseEntity.ok().body(memberService.findForgotPassword(forgotPasswordRequestDto));
    }
    @GetMapping("/applicant-list")
    public ResponseEntity<?> getApplicantList(Principal principal){
        return ResponseEntity.ok().body(memberService.findApplicantList(principal.getName()));
    }
    @PostMapping("/email-verify")
    public ResponseEntity<?> idAndEmailVerify(@RequestBody MemberRequestDto.IdAndEmailVerifyRequestDto verifyRequestDto){
        return ResponseEntity.ok().body(memberService.verifyIdAndEmail(verifyRequestDto));
    }

    @PostMapping("/idEmail-verify")
    public ResponseEntity<?> idAndEmailVerify(@RequestBody MemberRequestDto.NickNameAndEmailVerifyRequestDto verifyRequestDto){
        return ResponseEntity.ok().body(memberService.verifyNicknameAndEmail(verifyRequestDto));
    }
}