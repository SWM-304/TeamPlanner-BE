package com.tbfp.teamplannerbe.domain.common.exception;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public class ApplicationException extends RuntimeException {
    private ApplicationErrorType errorType;

    public ApplicationException(ApplicationErrorType errorType) {
        this.errorType = errorType;
    }
}
