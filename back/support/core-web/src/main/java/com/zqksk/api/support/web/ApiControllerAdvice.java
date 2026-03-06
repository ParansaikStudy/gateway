package com.zqksk.api.support.web;

import com.zqksk.api.support.error.AuthServiceError;
import com.zqksk.api.support.error.DefaultCoreErrorType;
import com.zqksk.api.support.error.DefaultServerError;
import com.zqksk.api.support.error.ErrorResponse;
import com.zqksk.api.support.exception.CoreErrorKind;
import com.zqksk.api.support.exception.CoreErrorLevel;
import com.zqksk.api.support.exception.CoreErrorType;
import com.zqksk.api.support.exception.CoreException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.reactive.function.client.WebClientResponseException;

@RestControllerAdvice
public class ApiControllerAdvice {
    private final Logger logger = LoggerFactory.getLogger(ApiControllerAdvice.class);

    @ExceptionHandler(CoreException.class)
    public ResponseEntity<ErrorResponse> handleCoreException(CoreException e) {
        switch (e.getErrorType().getLevel()) {
            case CoreErrorLevel.ERROR:
                logger.error("CoreException: {}", e.getMessage(), e);
                break;
            case CoreErrorLevel.WARN:
                logger.warn("CoreException: {}", e.getMessage(), e);
                break;
            default:
                logger.info("CoreException: {}", e.getMessage(), e);
        }

        HttpStatus status = switch (e.getErrorType().getKind()) {
            case CoreErrorKind.AUTHORIZATION -> HttpStatus.UNAUTHORIZED;
            case CoreErrorKind.CLIENT -> HttpStatus.BAD_REQUEST;
            default -> HttpStatus.INTERNAL_SERVER_ERROR;
        };

        return new ResponseEntity<>(new ErrorResponse(e.getErrorType(), e.getPayload()), status);
    }

    @ExceptionHandler(WebClientResponseException.class)
    public ResponseEntity<ErrorResponse> handleWebClientException(WebClientResponseException e) {
        HttpStatusCode statusCode = e.getStatusCode();

        CoreErrorKind errorKind = determineErrorKind(statusCode);
        CoreErrorLevel errorLevel = determineErrorLevel(statusCode);

        CoreErrorType errorType = new DefaultCoreErrorType(
                statusCode.toString(),
                "WebClientError",
                errorLevel,
                errorKind
        );

        logException(e, errorLevel);

        return new ResponseEntity<>(
                new ErrorResponse(errorType, e.getResponseBodyAsString()),
                statusCode
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(Exception e) {
        logger.error("Unexpected Exception: {}", e.getMessage(), e);

        CoreErrorType errorType;
        HttpStatus status;

        if (e instanceof WebClientResponseException.Unauthorized) {
            errorType = new AuthServiceError();
            status = HttpStatus.UNAUTHORIZED;
        } else if (e instanceof SecurityException) {
            errorType = new DefaultCoreErrorType(
                    "SECURITY_ERROR",
                    "Security related exception",
                    CoreErrorLevel.ERROR,
                    CoreErrorKind.AUTHORIZATION
            );
            status = HttpStatus.FORBIDDEN;
        } else {
            errorType = new DefaultServerError();
            status = HttpStatus.INTERNAL_SERVER_ERROR;
        }

        return new ResponseEntity<>(new ErrorResponse(errorType, null), status);
    }

    private CoreErrorKind determineErrorKind(HttpStatusCode statusCode) {
        if (statusCode == HttpStatus.UNAUTHORIZED || statusCode == HttpStatus.FORBIDDEN) {
            return CoreErrorKind.AUTHORIZATION;
        }
        if (statusCode == HttpStatus.BAD_REQUEST || statusCode == HttpStatus.NOT_FOUND) {
            return CoreErrorKind.CLIENT;
        }
        return CoreErrorKind.INTERNAL;
    }

    private CoreErrorLevel determineErrorLevel(HttpStatusCode statusCode) {
        int statusCodeValue = statusCode.value();
        if (statusCodeValue >= 500) return CoreErrorLevel.ERROR;
        if (statusCodeValue >= 400) return CoreErrorLevel.WARN;
        return CoreErrorLevel.INFO;
    }

    private void logException(Exception e, CoreErrorLevel level) {
        switch (level) {
            case CoreErrorLevel.ERROR:
                logger.error("Exception details: ", e);
                break;
            case CoreErrorLevel.WARN:
                logger.warn("Exception details: ", e);
                break;
            default:
                logger.info("Exception details: ", e);
        }
    }
}
