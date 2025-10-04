package com.eduai.quest.model.dto.quiz;

import lombok.Data;

import java.util.List;

@Data
public class QuizAttemptRequest {
    private List<AnswerDto> answers;
}