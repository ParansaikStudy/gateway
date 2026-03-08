package com.zqksk.api.support.exception;

public interface CoreErrorType {
    CoreErrorKind getKind();
    String getCode();
    String getMessage();
    CoreErrorLevel getLevel();
}
