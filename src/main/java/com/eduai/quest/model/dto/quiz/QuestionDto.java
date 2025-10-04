package com.eduai.quest.model.dto.quiz;

import com.eduai.quest.model.entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class QuestionDto {
    private Long id;
    private String text;
    private Question.QuestionType type;
    private List<String> options;
    private String correctOption;
}