package com.eduai.quest.model.dto.quiz;

import com.eduai.quest.model.entity.Course;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class GenerateQuizRequest {
    @NotBlank
    private String topic;

    @NotNull
    private Course.Difficulty difficulty;

    @Min(1) @Max(50)
    private Integer questionCount;

    private Boolean save = true;

    private Long courseId;
    private Long moduleId;
}