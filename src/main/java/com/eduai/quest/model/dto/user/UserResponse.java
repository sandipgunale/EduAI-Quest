package com.eduai.quest.model.dto.user;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class UserResponse {
    private Long id;
    private String email;
    private String name;
    private Set<String> roles;
    private boolean enabled;
    private LocalDateTime createdAt;
}