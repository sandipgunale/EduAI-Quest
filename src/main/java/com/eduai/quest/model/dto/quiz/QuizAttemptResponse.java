package com.eduai.quest.model.dto.quiz;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class QuizAttemptResponse {
    private Long attemptId;
    private Integer score;
    private Integer totalQuestions;
    private Double percentage;
    private LocalDateTime attemptedAt;
}