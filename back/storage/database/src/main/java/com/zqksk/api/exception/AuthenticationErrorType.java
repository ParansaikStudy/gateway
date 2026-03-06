package com.zqksk.api.exception;

import com.zqksk.api.support.exception.CoreErrorKind;
import com.zqksk.api.support.exception.CoreErrorLevel;
import com.zqksk.api.support.exception.CoreErrorType;

public enum AuthenticationErrorType implements CoreErrorType {
    NO_AUTHORIZATION(CoreErrorKind.AUTHORIZATION, "A01", "Authorization 이 존재하지 않습니다.", CoreErrorLevel.ERROR),
    INVALID_USER(CoreErrorKind.AUTHORIZATION, "A02", "존재하지 않는 사용자입니다.", CoreErrorLevel.ERROR),
    INVALID_AUTHORIZATION(CoreErrorKind.AUTHORIZATION, "A03", "유효하지 않은 Authorization 입니다.", CoreErrorLevel.ERROR),
    NOT_FOUND_TOKEN(CoreErrorKind.AUTHORIZATION, "A04", "토큰 조회에 실패하였습니다.", CoreErrorLevel.ERROR),
    INVALID_KEY(CoreErrorKind.AUTHORIZATION, "A05", "존재하지 않는 Key입니다.", CoreErrorLevel.ERROR),
    INVALID_AUTHENTICATION_CODE(CoreErrorKind.AUTHORIZATION, "A06", "메일 인증번호가 일치하지 않습니다.", CoreErrorLevel.ERROR),
    INVALID_VERIFICATION_CODE(CoreErrorKind.AUTHORIZATION, "A07", "비밀번호 변경 인증번호가 일치하지 않습니다.", CoreErrorLevel.ERROR),
    ALREADY_EXIST_USER(CoreErrorKind.AUTHORIZATION, "A08", "이미 가입한 사용자입니다.", CoreErrorLevel.ERROR),
    INVALID_ID_OR_PASSWORD(CoreErrorKind.AUTHORIZATION, "A09", "아이디 또는 비밀번호가 일치하지 않습니다.", CoreErrorLevel.ERROR),;

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
