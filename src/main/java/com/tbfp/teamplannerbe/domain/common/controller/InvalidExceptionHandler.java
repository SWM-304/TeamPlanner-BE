package com.tbfp.teamplannerbe.domain.common.controller;

import com.amazonaws.AmazonServiceException;
import com.tbfp.teamplannerbe.domain.common.controller.response.CMResDto;
import com.tbfp.teamplannerbe.domain.common.controller.response.ValidationErrorDto;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.support.MissingServletRequestPartException;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RestControllerAdvice // 모든 exception을 낚아챔
@Slf4j
public class InvalidExceptionHandler {

    /**
     *
     * Custom ApplicationException
     */

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<?> ApplicationException(ApplicationException e){
        return new ResponseEntity<>(new CMResDto<>(e.getErrorType().getCode(),e.getErrorType().getMessage(),""), e.getErrorType().getHttpStatus());
    }


    /**
     *
     * 유효성검사에 실패하는
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<?> argumentNotValidException(MethodArgumentNotValidException ex) {
        List<FieldError> fieldErrors = ex.getBindingResult().getFieldErrors();

        List<ValidationErrorDto> validationErrors = fieldErrors.stream()
                .map(error -> new ValidationErrorDto(error.getField(), error.getDefaultMessage()))
                .collect(Collectors.toList());

        return new ResponseEntity<>(new CMResDto<>(-1, "Validation failed", validationErrors), HttpStatus.BAD_REQUEST);
    }


    /**
     *  주로 파일 업로드나 멀티파트 요청에서 파트나 매개변수가 누락된 경우에 해당 예외
     */

    @ExceptionHandler(MissingServletRequestPartException.class)
    public ResponseEntity<?> missingServletRequestPartException(MissingServletRequestPartException exception) {
//        Sentry.captureException(exception);
        log.error("MissingServletRequestPartException = {}", exception);
        return ResponseEntity.badRequest().body("MissingServletRequestPartException");
    }
    /**
     *
     * 유효성검사 타입 불일치
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<?> methodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {

        log.error("MethodArgumentTypeMismatchException = {}", exception);
        return ResponseEntity.badRequest().body("잘못된 형식의 값입니다.");
    }
    @ExceptionHandler(AmazonServiceException.class)
    public ResponseEntity<?> amazonServiceException(AmazonServiceException exception) {
        log.error("AmazonServiceException = {}", exception);
        return ResponseEntity.badRequest().body("AmazonServiceException");
    }
}

