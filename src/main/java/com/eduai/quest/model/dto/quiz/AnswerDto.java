package com.eduai.quest.model.dto.quiz;

import lombok.Data;

@Data
public class AnswerDto {
    private Long questionId;
    private String answer;
}