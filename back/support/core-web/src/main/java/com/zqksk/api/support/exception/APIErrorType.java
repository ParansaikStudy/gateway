package com.zqksk.api.support.exception;

public enum APIErrorType implements CoreErrorType {
    BAD_REQUEST(CoreErrorKind.CLIENT, "R01", "올바르지 않은 요청입니다.", CoreErrorLevel.ERROR);

    private final CoreErrorKind kind;
    private final String code;
    private final String message;
    private final CoreErrorLevel level;

    APIErrorType(CoreErrorKind kind, String code, String message, CoreErrorLevel level) {
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
