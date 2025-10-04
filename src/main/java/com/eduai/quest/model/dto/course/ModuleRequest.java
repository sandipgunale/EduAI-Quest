package com.eduai.quest.model.dto.course;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ModuleRequest {
    @NotBlank
    private String title;

    private String description;

    private Integer orderIndex;

    private List<LessonRequest> lessons;
}