package com.eduai.quest.model.dto.user;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateRequest {
    @Size(min = 3, max = 50)
    private String name;

    private String password;

    private Boolean enabled;
}