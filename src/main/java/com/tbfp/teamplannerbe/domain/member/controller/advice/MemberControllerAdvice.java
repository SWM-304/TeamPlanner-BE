package com.tbfp.teamplannerbe.domain.member.controller.advice;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import io.sentry.Sentry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.security.Principal;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
@Slf4j
public class MemberControllerAdvice {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MemberResponseDto.ErrorResponseDto> validationHandler(Principal principal,MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstError = bindingResult.getFieldErrors().get(0);



        Map<String,String> errorMap=new HashMap<>();
        for(FieldError error:bindingResult.getFieldErrors()){
            // 핸들러 메서드의 타겟 오브젝트(컨트롤러)에서 메서드 이름 가져오기
            Object target = bindingResult.getTarget();
            String methodName = ex.getParameter().getMethod().getName();
            Class<?> type = target.getClass();

            errorMap.put(error.getField(), error.getDefaultMessage());

            log.info(principal.getName()+"님 : "+type+" : "+methodName+"() => 필드 : "+error.getField()+"메세지 : "+error.getDefaultMessage());
            Sentry.captureMessage(principal.getName()+"님의"+type+"."+methodName+"() => 필드:"+error.getField()+"메세지"+error.getDefaultMessage());
        }
        String errorField = firstError.getField();
        String errorDefaultMessage = firstError.getDefaultMessage();
        if(errorField!=null){

            String errorCode = "SIGNUP_REQUEST_INVALID_" + errorField.toUpperCase();
            ApplicationErrorType applicationErrorType = ApplicationErrorType.valueOf(errorCode);
            HttpStatus httpStatus = applicationErrorType.getHttpStatus();
            Integer code = applicationErrorType.getCode();
//            String message = applicationErrorType.getMessage();
            MemberResponseDto.ErrorResponseDto errorResponseDto =
                    MemberResponseDto.ErrorResponseDto.builder().
                            httpStatus(httpStatus).
                            code(code).
                            message(errorDefaultMessage).
                            build();
            return ResponseEntity.badRequest().body(errorResponseDto);
        }
        MemberResponseDto.ErrorResponseDto errorResponseDto= MemberResponseDto.ErrorResponseDto.builder().
                message("Error not occured").
                build();
        return ResponseEntity.ok().body(errorResponseDto);
    }
}