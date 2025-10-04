package com.eduai.quest.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "app.jwt")
public class JwtConfig {
    private String secret;
    private int accessTokenExpirationMinutes;
    private int refreshTokenExpirationDays;
}