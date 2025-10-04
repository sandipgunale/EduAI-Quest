package com.eduai.quest.model.dto.progress;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EnrollmentResponse {
    private Long id;
    private Long courseId;
    private String courseTitle;
    private String status;
    private Double progress;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
}