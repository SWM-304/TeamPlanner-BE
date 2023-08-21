package com.tbfp.teamplannerbe.domain.common.controller.response;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@Builder
public class ValidationErrorDto {
    private String field;
    private String message;

    public ValidationErrorDto(String field, String message) {
        this.field = field;
        this.message = message;
    }

}
