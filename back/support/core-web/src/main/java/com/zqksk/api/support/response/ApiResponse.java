package com.zqksk.api.support.response;

public sealed interface ApiResponse permits ApiResponse.Success {
    int DEFAULT_SUCCESS_CODE = 200;
    int CREATE_SUCCESS_CODE = 201;
    String DEFAULT_SUCCESS_MESSAGE = "요청이 성공했습니다.";
    String CREATE_SUCCESS_MESSAGE = "저장이 성공했습니다.";

    static <T> ApiResponse of(String message, T data) {
        return new Success<>(DEFAULT_SUCCESS_CODE, message, data);
    }

    static <T> ApiResponse of(T data) {
        return new Success<>(DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, data);
    }

    static <T> ApiResponse created(T data) {
        return new Success<>(CREATE_SUCCESS_CODE, CREATE_SUCCESS_MESSAGE, data);
    }

    record Success<T>(
            int status,
            String message,
            T data
    ) implements ApiResponse {
    }
}
