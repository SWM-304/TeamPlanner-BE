package com.tbfp.teamplannerbe.domain.member;

public enum ErrorCode {
    INVALID_USERNAME,
    DUPLICATE_USERNAME,
    INVALID_PASSWORD,
    INVALID_EMAIL,
    INVALID_PHONE,
    INVALID_CONTACTEMAIL,
    INVALID_DEFAULT;

    public static ErrorCode mapToErrorCode(String fieldName) {
        try {
            return ErrorCode.valueOf("INVALID_" + fieldName.toUpperCase());
        } catch (IllegalArgumentException e) {
            return INVALID_DEFAULT; //
        }
    }
}