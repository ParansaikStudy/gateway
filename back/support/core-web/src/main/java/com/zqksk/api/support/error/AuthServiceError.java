package com.zqksk.api.support.error;

import com.zqksk.api.support.exception.CoreErrorKind;
import com.zqksk.api.support.exception.CoreErrorLevel;
import com.zqksk.api.support.exception.CoreErrorType;

public class AuthServiceError implements CoreErrorType {
    private static final CoreErrorKind kind = CoreErrorKind.AUTHORIZATION;
    private static final String code = "A401";
    private static final String message = "인증이 실패했습니다.";
    private static final CoreErrorLevel level = CoreErrorLevel.ERROR;

    @Override
    public CoreErrorKind getKind() {
        return kind;
    }

    @Override
    public String getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public CoreErrorLevel getLevel() {
        return level;
    }
}