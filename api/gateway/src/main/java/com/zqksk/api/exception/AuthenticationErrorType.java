package com.zqksk.api.exception;

import com.zqksk.api.support.exception.CoreErrorKind;
import com.zqksk.api.support.exception.CoreErrorLevel;
import com.zqksk.api.support.exception.CoreErrorType;

public enum AuthenticationErrorType implements CoreErrorType {
    NO_AUTHORIZATION(CoreErrorKind.AUTHORIZATION, "401", "Authorization 이 존재하지 않습니다.", CoreErrorLevel.ERROR),
    INVALID_USER(CoreErrorKind.AUTHORIZATION, "401", "존재하지 않는 사용자입니다.", CoreErrorLevel.ERROR),
    INVALID_AUTHORIZATION(CoreErrorKind.AUTHORIZATION, "401", "유효하지 않은 Authorization 입니다.", CoreErrorLevel.ERROR);

    private final CoreErrorKind kind;
    private final String code;
    private final String message;
    private final CoreErrorLevel level;

    AuthenticationErrorType(CoreErrorKind kind, String code, String message, CoreErrorLevel level) {
        this.kind = kind;
        this.code = code;
        this.message = message;
        this.level = level;
    }

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
