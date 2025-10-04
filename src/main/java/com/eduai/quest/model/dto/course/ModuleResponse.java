package com.eduai.quest.model.dto.course;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ModuleResponse {
    private Long id;
    private String title;
    private String description;
    private Integer orderIndex;
    private Long courseId;
    private LocalDateTime createdAt;
    private List<LessonResponse> lessons;
}