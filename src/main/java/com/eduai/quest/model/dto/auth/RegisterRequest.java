package com.eduai.quest.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Email
    private String email;

    @NotBlank @Size(min = 3, max = 50)
    private String name;

    @NotBlank @Size(min = 8)
    private String password;

    private String role; // STUDENT, TEACHER, ADMIN
}