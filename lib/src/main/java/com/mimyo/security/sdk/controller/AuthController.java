package com.mimyo.security.sdk.controller;

import com.mimyo.security.sdk.AuthResult;
import com.mimyo.security.sdk.TokenValidationResult;
import com.mimyo.security.sdk.exception.AuthenticationException;
import com.mimyo.security.sdk.impl.JwtAuthenticationService;
import com.mimyo.security.sdk.model.Role;
import com.mimyo.security.sdk.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final JwtAuthenticationService authenticationService;

    public AuthController(JwtAuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        try {
            AuthResult result = authenticationService.authenticate(
                    loginRequest.username(),
                    loginRequest.password()
            );

            return ResponseEntity.ok(new LoginResponse(result.token(), result.userInfo()));
        } catch (AuthenticationException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestBody TokenRequest tokenRequest) {
        TokenValidationResult result = authenticationService.validateToken(tokenRequest.token());

        if (result.valid()) {
            return ResponseEntity.ok(Map.of(
                    "valid", true,
                    "claims", result.claims().orElse(Map.of())
            ));
        } else {
            return ResponseEntity.badRequest().body(Map.of(
                    "valid", false,
                    "error", result.errorMessage()
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        try {
            User user = authenticationService.createUser(
                    request.username(),
                    request.password(),
                    request.name(),
                    request.email(),
                    Set.of(Role.USER)
            );

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(Map.of(
                            "id", user.getId(),
                            "username", user.getUsername(),
                            "name", user.getName(),
                            "email", user.getEmail()
                    ));
        } catch (AuthenticationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/permissions")
    public ResponseEntity<?> getPermissions(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Authorization header required"));
        }

        String token = authHeader.substring(7);
        Map<String, Object> claims = authenticationService.extractClaims(token);

        if (claims.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid token"));
        }

        @SuppressWarnings("unchecked")
        List<String> permissions = (List<String>) claims.getOrDefault("auth", List.of());

        return ResponseEntity.ok(Map.of("permissions", permissions));
    }

    // Request/Response records
    public record LoginRequest(String username, String password) {}
    public record LoginResponse(String token, Map<String, Object> userInfo) {}
    public record TokenRequest(String token) {}
    public record RegisterRequest(String username, String password, String name, String email) {}
}