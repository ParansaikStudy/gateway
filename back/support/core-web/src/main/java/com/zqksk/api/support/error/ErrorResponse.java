package com.zqksk.api.support.error;

import com.zqksk.api.support.exception.CoreErrorType;
import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String code;
    private final String message;
    private final Object payload;

    private ErrorResponse(String code, String message, Object payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }

    public ErrorResponse(CoreErrorType errorType, Object payload) {
        this(errorType.getCode(), errorType.getMessage(), payload);
    }

}


