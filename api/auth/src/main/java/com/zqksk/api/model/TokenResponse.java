package com.zqksk.api.model;

public record TokenResponse(
        String tokenType,
        String accessToken,
        String refreshToken,
        long expiresIn
) {
    public static TokenResponse from(String accessToken, String refreshToken, Long expiresIn) {
        return new TokenResponse("Bearer", accessToken, refreshToken, expiresIn);
    }
}
