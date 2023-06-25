package com.tbfp.teamplannerbe.domain.member.controller;

import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.MemberLoginRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberRequestDto.MemberRenewAccessTokenRequestDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto.MemberRenewAccessTokenResponseDto;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/member")
@RequiredArgsConstructor
@Slf4j
@Tag(name= "MemberController", description = "멤버 API")
public class MemberController {

    private final MemberService memberService;

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
    public ResponseEntity renewAccessToken(@RequestBody MemberRenewAccessTokenRequestDto memberRenewAccessTokenRequestDto) {
        log.info("memberRenewAccessTokenRequestDto.getRefreshToken() = " + memberRenewAccessTokenRequestDto.getRefreshToken());
        return ResponseEntity.ok(MemberRenewAccessTokenResponseDto.builder()
                                                                .accessToken(
                memberService.renewAccessToken(memberRenewAccessTokenRequestDto.getRefreshToken())
        ).build()
        );
    }
}
