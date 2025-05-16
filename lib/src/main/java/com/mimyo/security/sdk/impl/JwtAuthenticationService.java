package com.mimyo.security.sdk.impl;

import com.mimyo.security.sdk.AuthResult;
import com.mimyo.security.sdk.AuthenticationService;
import com.mimyo.security.sdk.TokenValidationResult;
import com.mimyo.security.sdk.exception.AuthenticationException;
import com.mimyo.security.sdk.model.Role;
import com.mimyo.security.sdk.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.*;


@Service
public class JwtAuthenticationService implements AuthenticationService {

    private final SecretKey key;
    private final InMemoryUserRepository userRepository;
    private final long tokenValidityInMilliseconds;

    public JwtAuthenticationService(
            @Value("${security.sdk.jwt.secret:defaultSecretKey}") String secret,
            @Value("${security.sdk.jwt.token-validity-in-seconds:86400}") long tokenValidityInSeconds,
            InMemoryUserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.tokenValidityInMilliseconds = tokenValidityInSeconds * 1000;
        this.userRepository = userRepository;
    }

    @Override
    public AuthResult authenticate(String username, String password) throws AuthenticationException {
        return userRepository.findByUsername(username)
                .filter(user -> user.passwordMatches(password))
                .map(user -> {
                    Map<String, Object> claims = new HashMap<>();
                    claims.put("sub", user.getUsername());
                    claims.put("auth", user.getAuthorities());
                    claims.put("name", user.getName());

                    String token = generateToken(claims);

                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("username", user.getUsername());
                    userInfo.put("name", user.getName());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("roles", user.getRoles());

                    return AuthResult.success(token, userInfo);
                })
                .orElseThrow(() -> new AuthenticationException("Invalid username or password"));
    }

    @Override
    public AuthResult authenticateWithToken(String token) throws AuthenticationException {
        TokenValidationResult result = validateToken(token);

        if (!result.valid()) {
            throw new AuthenticationException(result.errorMessage());
        }

        Map<String, Object> claims = result.claims().orElse(new HashMap<>());
        String username = (String) claims.get("sub");

        return userRepository.findByUsername(username)
                .map(user -> {
                    Map<String, Object> userInfo = new HashMap<>();
                    userInfo.put("username", user.getUsername());
                    userInfo.put("name", user.getName());
                    userInfo.put("email", user.getEmail());
                    userInfo.put("roles", user.getRoles());

                    return AuthResult.success(token, userInfo);
                })
                .orElseThrow(() -> new AuthenticationException("User not found"));
    }

    @Override
    public String generateToken(Map<String, Object> claims) {
        long now = System.currentTimeMillis();
        Date validity = new Date(now + tokenValidityInMilliseconds);

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(now))
                .setExpiration(validity)
                .signWith(key, SignatureAlgorithm.HS256)  // 알고리즘 명시적으로 지정
                .compact();
    }

    @Override
    public TokenValidationResult validateToken(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            // 토큰 만료 체크
            if (claims.getExpiration().before(new Date())) {
                return TokenValidationResult.invalid("Token has expired");
            }

            return TokenValidationResult.valid(new HashMap<>(claims));
        } catch (Exception e) {
            return TokenValidationResult.invalid("Invalid token: " + e.getMessage());
        }
    }

    @Override
    public Map<String, Object> extractClaims(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();


            return new HashMap<>(claims);
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    @Override
    public boolean hasPermission(String token, String permission) {
        Map<String, Object> claims = extractClaims(token);

        if (claims.isEmpty()) {
            return false;
        }

        @SuppressWarnings("unchecked")
        List<String> authorities = (List<String>) claims.getOrDefault("auth", List.of());

        return authorities.contains(permission) || authorities.contains("*");
    }

    // 추가 사용자 관리 메서드
    public User createUser(String username, String password, String name, String email, Set<Role> roles) {
        if (userRepository.existsByUsername(username)) {
            throw new AuthenticationException("Username already exists");
        }

        return userRepository.createUser(username, password, name, email, roles);
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }
}