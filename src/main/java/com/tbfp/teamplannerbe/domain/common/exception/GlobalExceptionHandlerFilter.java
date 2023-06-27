package com.tbfp.teamplannerbe.domain.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@Component
public class GlobalExceptionHandlerFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            filterChain.doFilter(request,response);
        } catch (ApplicationException ex){
            log.info("ApplicationException ex = " + ex);
            response.setContentType("application/json");
            response.setCharacterEncoding("utf-8");
            ApplicationErrorType errorType = ex.getErrorType();
            response.setStatus(errorType.getHttpStatus().value());
            response.getWriter().write(new ObjectMapper().writeValueAsString(errorType));
        }catch (Exception ex){
            log.error("exception exception handler filter");
            log.info("ex.getMessage() = " + ex.getMessage());
            ex.printStackTrace();
            response.setStatus(HttpStatus.BAD_REQUEST.value());
            response.setCharacterEncoding("utf-8");
            response.setContentType("application/json");
        }
    }
}
