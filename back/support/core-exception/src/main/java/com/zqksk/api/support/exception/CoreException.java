package com.zqksk.api.support.exception;

import lombok.Getter;

@Getter
public class CoreException extends RuntimeException {
    private final CoreErrorType errorType;
    private final Object payload;

    public CoreException(CoreErrorType errorType) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.payload = null;
    }

    public CoreException(CoreErrorType errorType, Object payload) {
        super(errorType.getMessage());
        this.errorType = errorType;
        this.payload = payload;
    }
}
