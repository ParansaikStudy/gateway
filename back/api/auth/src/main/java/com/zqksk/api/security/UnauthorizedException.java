package com.zqksk.api.security;

import com.zqksk.api.support.error.AuthServiceError;
import com.zqksk.api.support.exception.CoreException;
import lombok.Getter;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.jwt.BadJwtException;
import org.springframework.security.oauth2.jwt.JwtValidationException;
import org.springframework.security.oauth2.server.resource.InvalidBearerTokenException;

import java.util.HashMap;
import java.util.Map;

@Getter
public sealed class UnauthorizedException extends AuthenticationException {

    private final int errorCode;

    public UnauthorizedException(String msg, int errorCode) {
        super(msg);
        this.errorCode = errorCode;
    }

    public static final class InvalidAuthentication extends UnauthorizedException {
        public static final InvalidAuthentication INSTANCE = new InvalidAuthentication();

        private InvalidAuthentication() {
            super("아이디 또는 비밀번호를 잘못 입력했습니다.", 400);
        }
    }

    public static final class InvalidToken extends UnauthorizedException {
        public static final InvalidToken INSTANCE = new InvalidToken();

        private InvalidToken() {
            super("유효하지 않은 토큰입니다.", 401);
        }
    }

    public static final class ExpiredToken extends UnauthorizedException {
        public static final ExpiredToken INSTANCE = new ExpiredToken();

        private ExpiredToken() {
            super("만료된 토큰입니다.", 401);
        }
    }

    public static final class UnExpected extends UnauthorizedException {
        public static final UnExpected INSTANCE = new UnExpected();

        private UnExpected() {
            super("알 수 없는 오류가 발생했습니다.", 500);
        }
    }

    public static CoreException getUnauthorizedExceptionMessage(AuthenticationException e) {
        UnauthorizedException unauthorizedException = parseException(e);
        Map<String, Object> map = new HashMap<>();
        map.put("code", unauthorizedException.getErrorCode());
        map.put("message", unauthorizedException.getMessage());
        return new CoreException(new AuthServiceError(), map);
    }

    private static UnauthorizedException parseException(AuthenticationException e) {
        return switch (e) {
            case UnauthorizedException unauthorizedException -> unauthorizedException;
            case InvalidBearerTokenException invalidBearerTokenException -> {
                Throwable cause = invalidBearerTokenException.getCause();
                yield switch (cause) {
                    case JwtValidationException ignored -> ExpiredToken.INSTANCE;
                    case BadJwtException ignored -> InvalidToken.INSTANCE;
                    default -> UnExpected.INSTANCE;
                };
            }
            default -> UnExpected.INSTANCE;
        };
    }
}