package com.eduai.quest.model.dto.auth;

import lombok.Data;

import java.util.Set;

@Data
public class JwtResponse {
    private String accessToken;
    private String refreshToken;
    private Long expiresIn;
    private String tokenType = "Bearer";
    private String email;
    private String name;
    private Set<String> roles;
}