package com.eduai.quest.model.dto.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LessonRequest {
    @NotBlank
    private String title;

    private String content;

    private String videoUrl;

    private String filePath;

    private Integer orderIndex;
}