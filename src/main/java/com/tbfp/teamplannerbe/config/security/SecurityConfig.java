package com.tbfp.teamplannerbe.config.security;


import com.tbfp.teamplannerbe.domain.auth.JwtAuthenticationFilter;
import com.tbfp.teamplannerbe.domain.auth.JwtAuthorizationFilter;
import com.tbfp.teamplannerbe.domain.auth.JwtProvider;
import com.tbfp.teamplannerbe.domain.auth.oauth.CustomOAuth2MemberService;
import com.tbfp.teamplannerbe.domain.auth.oauth.OAuth2LoginFailureHandler;
import com.tbfp.teamplannerbe.domain.auth.oauth.OAuth2LoginSuccessHandler;
import com.tbfp.teamplannerbe.domain.auth.service.RefreshTokenService;
import com.tbfp.teamplannerbe.domain.common.exception.GlobalExceptionHandlerFilter;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final JwtProvider jwtProvider;
    private final MemberService memberService;
    private final RefreshTokenService refreshTokenService;
    private final AuthenticationConfiguration authenticationConfiguration;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2MemberService customOAuth2MemberService;
    private final GlobalExceptionHandlerFilter globalExceptionHandlerFilter;
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
//                .addFilterBefore(new LogFilter(), CorsFilter.class)
                .formLogin().disable() // 초기 로그인화면 자동생성 안함
//                .httpBasic().disable()  // 요청마다 Authorization 헤더에 아이디,패스워드 base64 인코딩한 문자열 보내야함 default : disable
                .csrf().disable() // rest api 에서 csrf 방어 필요 없음
                .cors().configurationSource(corsConfigurationSource()).and() // cors
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()// 세션 생성 안함 , response 에 setcookie jsessionId= ... 안함
                .addFilter(new JwtAuthenticationFilter(authenticationManager(), refreshTokenService, jwtProvider))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), memberService, jwtProvider))
                .authorizeRequests()
                .antMatchers(
                        "/api/v1/**",
                        "/", "/api/v1/member/login", "/api/v1/member/renew-access-token"
                        , "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**" // swagger
                ).permitAll()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                .successHandler(oAuth2LoginSuccessHandler) // 동의하고 계속하기를 눌렀을 때 Handler 설정
                .failureHandler(oAuth2LoginFailureHandler) // 소셜 로그인 실패 시 핸들러 설정
                .userInfoEndpoint().userService(customOAuth2MemberService);
        httpSecurity.addFilterAfter(globalExceptionHandlerFilter, LogoutFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOriginPattern("*localhost*"); // A list of origins for which cross-origin requests are allowed. ex) http://localhost:8080
        configuration.addAllowedHeader("*"); // Set the HTTP methods to allow ,ex) "GET", "POST", "PUT";
        configuration.addAllowedMethod("*"); // Set the list of headers that a pre-flight request can list as allowed for use during an actual request. ex) "Authorization", "Cache-Control", "Content-Type"
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
