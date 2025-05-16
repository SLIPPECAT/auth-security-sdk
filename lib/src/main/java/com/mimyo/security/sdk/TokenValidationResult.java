package com.mimyo.security.sdk;

import java.util.Map;
import java.util.Optional;

// 토큰 검증 결과
public record TokenValidationResult(
        boolean valid,
        String errorMessage,
        Optional<Map<String, Object>> claims
) {
    public static TokenValidationResult valid(Map<String, Object> claims) {
        return new TokenValidationResult(true, null, Optional.ofNullable(claims));
    }

    public static TokenValidationResult invalid(String errorMessage) {
        return new TokenValidationResult(false, errorMessage, Optional.empty());
    }
}