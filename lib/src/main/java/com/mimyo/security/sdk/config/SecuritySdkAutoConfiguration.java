package com.mimyo.security.sdk.config;

import com.mimyo.security.sdk.AuthenticationService;
import com.mimyo.security.sdk.impl.InMemoryUserRepository;
import com.mimyo.security.sdk.impl.JwtAuthenticationService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// SDK 자동 설정
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableConfigurationProperties(SecuritySdkProperties.class)
public class SecuritySdkAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    @ConditionalOnMissingBean
    public InMemoryUserRepository userRepository(BCryptPasswordEncoder passwordEncoder) {
        return new InMemoryUserRepository();
    }

    @Bean
    @ConditionalOnMissingBean
    public AuthenticationService authenticationService(
            SecuritySdkProperties properties,
            InMemoryUserRepository userRepository) {
        return new JwtAuthenticationService(
                properties.getJwt().getSecret(),
                properties.getJwt().getTokenValidityInSeconds(),
                userRepository
        );
    }
}
