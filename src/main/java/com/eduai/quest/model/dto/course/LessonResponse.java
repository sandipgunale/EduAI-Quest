package com.eduai.quest.model.dto.course;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LessonResponse {
    private Long id;
    private String title;
    private String content;
    private String videoUrl;
    private String filePath;
    private Integer orderIndex;
    private Long moduleId;
    private LocalDateTime createdAt;
}