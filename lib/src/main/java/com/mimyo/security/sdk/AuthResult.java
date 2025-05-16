package com.mimyo.security.sdk;

import java.util.Map;

// 인증 결과를 담는 클래스
public record AuthResult(
        boolean success,
        String token,
        String errorMessage,
        Map<String, Object> userInfo
) {
    public static AuthResult success(String token, Map<String, Object> userInfo) {
        return new AuthResult(true, token, null, userInfo);
    }

    public static AuthResult failure(String errorMessage) {
        return new AuthResult(false, null, errorMessage, null);
    }
}