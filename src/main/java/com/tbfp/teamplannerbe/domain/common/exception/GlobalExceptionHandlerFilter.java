package com.tbfp.teamplannerbe.domain.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.NestedServletException;

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
        } catch (NestedServletException exx) {
            log.info("GlobalExceptionHandlerFilter.doFilterInternal: catched NestedSeveletException");
            if (exx.getRootCause() instanceof ApplicationException) {
                log.info("Application Exception");
                ApplicationException ex = (ApplicationException) exx.getRootCause();
                response.setContentType("application/json");
                response.setCharacterEncoding("utf-8");
                ApplicationErrorType errorType = ex.getErrorType();
                response.setStatus(errorType.getHttpStatus().value());
                response.getWriter().write(new ObjectMapper().writeValueAsString(errorType));
            } else {
                log.info("Other Exception");
                throw exx;
            }
        } catch (Exception ex){
            throw ex;
        }
    }
}