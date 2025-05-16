package com.mimyo.security.sdk;

import javax.naming.AuthenticationException;
import java.util.Map;
import java.util.Optional;

/**
 * 인증 및 권한 부여 기능을 제공하는 핵심 인터페이스
 */
public interface AuthenticationService {
    /**
     * 사용자명과 비밀번호로 사용자 인증
     *
     * @param username 사용자명
     * @param password 비밀번호
     * @return 인증 결과
     * @throws com.mimyo.security.sdk.exception.AuthenticationException 인증 실패 시
     */
    AuthResult authenticate(String username, String password) throws AuthenticationException;

    /**
     * 토큰을 사용하여 사용자 인증
     *
     * @param token JWT 토큰
     * @return 인증 결과
     * @throws com.mimyo.security.sdk.exception.AuthenticationException 인증 실패 시
     */
    AuthResult authenticateWithToken(String token) throws AuthenticationException;

    /**
     * 제공된 클레임을 사용하여 JWT 토큰 생성
     *
     * @param claims 토큰에 포함될 클레임 맵
     * @return 생성된 JWT 토큰 문자열
     */
    String generateToken(Map<String, Object> claims);

    /**
     * JWT 토큰 유효성 검증
     *
     * @param token 검증할 JWT 토큰
     * @return 토큰 검증 결과
     */
    TokenValidationResult validateToken(String token);

    /**
     * JWT 토큰에서 클레임 추출
     *
     * @param token JWT 토큰
     * @return 추출된 클레임 맵, 토큰이 유효하지 않으면 빈 맵 반환
     */
    Map<String, Object> extractClaims(String token);

    /**
     * 사용자가 특정 권한을 가지고 있는지 확인
     *
     * @param token JWT 토큰
     * @param permission 확인할 권한 문자열
     * @return 권한이 있으면 true, 없으면 false
     */
    boolean hasPermission(String token, String permission);
}