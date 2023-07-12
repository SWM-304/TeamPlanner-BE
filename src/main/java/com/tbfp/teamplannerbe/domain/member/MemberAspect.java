package com.tbfp.teamplannerbe.domain.member;

import com.tbfp.teamplannerbe.domain.common.exception.ApplicationErrorType;
import com.tbfp.teamplannerbe.domain.common.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

@RequiredArgsConstructor
@Component
@Aspect
public class MemberAspect {

//    private final Validator validator;
//    @Pointcut("within(com.tbfp.teamplannerbe.domain.member.service.MemberService) && @args(javax.validation.Valid)")
//    public void validationPointcut(){ }
//    @Before("validationPointcut() && args(param)")
//    public void validateMember(Object param){
//        ConstraintViolation<Object> firstViolation = validator.validate(param).stream().findFirst().orElse(null);
//        if(firstViolation!=null){
//            firstViolation.getPropertyPath().toString();
//            String errorCode = "SIGNUP_REQUEST_INVALID_" + firstViolation.getPropertyPath().toString().toUpperCase();
//            throw new ApplicationException(ApplicationErrorType.valueOf(errorCode));
//        }
//    }
}