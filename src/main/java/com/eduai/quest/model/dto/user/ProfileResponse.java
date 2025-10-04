package com.eduai.quest.model.dto.user;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProfileResponse {
    private Long id;
    private String email;
    private String name;
    private String bio;
    private String avatarUrl;
    private LocalDateTime createdAt;
    private Integer enrolledCourses;
    private Integer completedCourses;
    private Integer totalQuizzes;
}