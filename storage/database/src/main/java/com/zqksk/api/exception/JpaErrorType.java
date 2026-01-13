package com.zqksk.api.exception;

import com.zqksk.api.support.exception.CoreErrorKind;
import com.zqksk.api.support.exception.CoreErrorLevel;
import com.zqksk.api.support.exception.CoreErrorType;

public enum JpaErrorType implements CoreErrorType {
    NOT_FOUND_DATA(CoreErrorKind.INTERNAL, "A01", "데이터를 찾을 수 없습니다.", CoreErrorLevel.ERROR);

    private final CoreErrorKind kind;
    private final String code;
    private final String message;
    private final CoreErrorLevel level;

    JpaErrorType(CoreErrorKind kind, String code, String message, CoreErrorLevel level) {
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
