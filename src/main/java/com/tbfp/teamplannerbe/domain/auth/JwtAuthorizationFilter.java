package com.tbfp.teamplannerbe.domain.auth;

import com.auth0.jwt.exceptions.TokenExpiredException;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import com.tbfp.teamplannerbe.domain.member.entity.Member;
import com.tbfp.teamplannerbe.domain.member.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final MemberService memberService;
    private final JwtProvider jwtProvider;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, MemberService memberService, JwtProvider jwtProvider) {
        super(authenticationManager);
        this.memberService = memberService;
        this.jwtProvider = jwtProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        log.info("JwtAuthorizationFilter.doFilterInternal");
        String path = request.getServletPath();
        System.out.println("path = " + path);
//        String header = request.getHeader(JwtProperties.HEADER_STRING);
        String header = jwtProvider.getHeader(request);
        if (header == null) {
            log.info("header = null");
//            throw new ApplicationException(ApplicationErrorType.EXPIRED_ACCESS_TOKEN);
//            chain.doFilter(request, response);
            return;
        }

        String username = null;
        try {
            username = jwtProvider.getUsername(request);
        } catch (TokenExpiredException e) {
            throw new ApplicationException(ApplicationErrorType.EXPIRED_ACCESS_TOKEN);
        }

        if (username != null) {
            log.info("username = " + username);
            Member member = memberService.findMemberByUsername(username).orElseThrow(() -> new ApplicationException(ApplicationErrorType.USER_NOT_FOUND));
            MemberDetails memberDetails = new MemberDetails(member);
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(memberDetails, null, null);
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        chain.doFilter(request, response);
    }
}