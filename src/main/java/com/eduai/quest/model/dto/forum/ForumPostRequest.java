package com.eduai.quest.model.dto.forum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForumPostRequest {
    @NotBlank
    private String title;

    @NotBlank
    private String content;

    private Long courseId;

    private Boolean pinned = false;
}