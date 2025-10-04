package com.eduai.quest.model.dto.course;

import com.eduai.quest.model.entity.Course;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class CourseRequest {
    @NotBlank
    private String title;

    private String description;

    @NotBlank
    private String category;

    @NotNull
    private Course.Difficulty difficulty;

    private List<ModuleRequest> modules;
}