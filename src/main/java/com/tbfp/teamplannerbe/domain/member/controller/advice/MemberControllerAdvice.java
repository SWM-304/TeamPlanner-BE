package com.tbfp.teamplannerbe.domain.member.controller.advice;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.member.dto.MemberResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class MemberControllerAdvice {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<MemberResponseDto.ErrorResponseDto> validationHandler(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        FieldError firstError = bindingResult.getFieldErrors().get(0);
        String errorField = firstError.getField();

        if(errorField!=null){
            String errorCode = "SIGNUP_REQUEST_INVALID_" + errorField.toUpperCase();
            ApplicationErrorType applicationErrorType = ApplicationErrorType.valueOf(errorCode);
            HttpStatus httpStatus = applicationErrorType.getHttpStatus();
            Integer code = applicationErrorType.getCode();
            String message = applicationErrorType.getMessage();
            MemberResponseDto.ErrorResponseDto errorResponseDto =
                    MemberResponseDto.ErrorResponseDto.builder().
                            httpStatus(httpStatus).
                            code(code).
                            message(message).
                            build();
            return ResponseEntity.badRequest().body(errorResponseDto);
        }
        MemberResponseDto.ErrorResponseDto errorResponseDto= MemberResponseDto.ErrorResponseDto.builder().
                message("Error not occured").
                build();
        return ResponseEntity.ok().body(errorResponseDto);
    }
}