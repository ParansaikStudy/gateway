package com.zqksk.api.support.error;

import com.zqksk.api.support.exception.CoreErrorKind;
import com.zqksk.api.support.exception.CoreErrorLevel;
import com.zqksk.api.support.exception.CoreErrorType;

public class DefaultCoreErrorType implements CoreErrorType {
    private final CoreErrorLevel level;
    private final CoreErrorKind kind;
    private final String code;
    private final String message;

    public DefaultCoreErrorType(String code, String message, CoreErrorLevel level, CoreErrorKind kind) {
        this.code = code;
        this.message = message;
        this.level = level;
        this.kind = kind;
    }

    @Override
    public CoreErrorLevel getLevel() {
        return level;
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
}
