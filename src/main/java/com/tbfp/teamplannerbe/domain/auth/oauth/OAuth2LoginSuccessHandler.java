package com.tbfp.teamplannerbe.domain.auth.oauth;

import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.MemberRole;
import com.tbfp.teamplannerbe.domain.auth.service.RefreshTokenService;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final JwtProvider jwtProvider;
    private final RefreshTokenService refreshTokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        log.info("OAuth2 Login 성공!");
        CustomOAuth2Member oAuth2Member = (CustomOAuth2Member) authentication.getPrincipal();

        // User의 Role이 GUEST일 경우 처음 요청한 회원이므로 회원가입 페이지로 가서 회원가입 마무리 해야함 (추가정보 입력 후..)
        if(oAuth2Member.getMemberRole() == MemberRole.GUEST) {
            throw new ApplicationException(ApplicationErrorType.GUEST_USER);
        } else {
            loginSuccess(response, oAuth2Member,getCurrentDomain(request)); // 로그인에 성공한 경우 access, refresh 토큰 생성
        }
    }

    // 현재 도메인 정보를 가져오는 메서드
    private String getCurrentDomain(HttpServletRequest request) {
        String scheme = request.getScheme();
        log.info("scheme = " + scheme);
        String serverName = request.getServerName();
        log.info("serverName = " + serverName);

        log.info("request.getLocalPort() = " + request.getLocalPort());
        log.info("request.getRemotePort() = " + request.getRemotePort());
        log.info("request.getServerPort() = " + request.getServerPort());

        String referer = request.getHeader("referer");
        log.info("referer = " + referer);
        String currentDomain="";

        if (referer == null) {
            // 소셜로그인 처음 성공하면 이럼 , 두번째 부터는 null 아님 프론트 주소 가져옴
            // TODO: 나중에 수정, 임시로 localhost:3000
            referer = "http://localhost:3000/";
        }
        currentDomain = referer;
        return currentDomain;
    }


    private void loginSuccess(HttpServletResponse response, CustomOAuth2Member oAuth2Member,String domain) {
        log.info("OAuth2LoginSuccessHandler.loginSuccess");
        log.info("살려줘"+domain);
        String accessToken = jwtProvider.generateAccessToken(oAuth2Member.getUsername());
        String refreshToken = jwtProvider.generateRefreshToken(oAuth2Member.getUsername());

        refreshTokenService.setRefreshToken(oAuth2Member.getUsername(), refreshToken);

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setStatus(HttpServletResponse.SC_OK);
//        CookieUtil.addCookie(response, "accessToken", accessToken, jwtProvider.ACCESS_TOKEN_EXPIRATION_TIME);
//        CookieUtil.addCookie(response, "refreshToken", refreshToken, jwtProvider.REFRESH_TOKEN_EXPIRATION_TIME);
        try {
            // token body comment
//            response.getWriter().write(
//                    new ObjectMapper().writeValueAsString(
//                            MemberResponseDto.MemberLoginResponseDto.builder()
//                                    .accessToken(accessToken)
//                                    .refreshToken(refreshToken)
//                                    .build()
//                    )
//            );
            response.sendRedirect(domain+"oauth2/redirect?accessToken=" + accessToken + "&refreshToken=" + refreshToken);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
