package com.tbfp.teamplannerbe.domain.member.controller;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.cookie.CookieUtil;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.MemberRenewAccessTokenRequestDto;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
}
