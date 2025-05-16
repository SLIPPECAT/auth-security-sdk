package com.mimyo.security.sdk.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

// SDK 설정을 위한 속성 클래스
@ConfigurationProperties(prefix = "security.sdk")
public class SecuritySdkProperties {
    private Jwt jwt = new Jwt();

    public Jwt getJwt() {
        return jwt;
    }

    public void setJwt(Jwt jwt) {
        this.jwt = jwt;
    }

    public static class Jwt {
        private String secret = "defaultSecretThatShouldBeOverriddenInProduction";
        private long tokenValidityInSeconds = 86400; // 24시간

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public long getTokenValidityInSeconds() {
            return tokenValidityInSeconds;
        }

        public void setTokenValidityInSeconds(long tokenValidityInSeconds) {
            this.tokenValidityInSeconds = tokenValidityInSeconds;
        }
    }
}