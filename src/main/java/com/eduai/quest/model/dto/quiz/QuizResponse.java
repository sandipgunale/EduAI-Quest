package com.eduai.quest.model.dto.quiz;

import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.Question;
import lombok.Data;

import java.util.List;

@Data
public class QuizResponse {
    private Long id;
    private String title;
    private Course.Difficulty difficulty;
    private Long courseId;
    private Long moduleId;
    private boolean generatedByAI;
    private List<QuestionDto> questions;
}