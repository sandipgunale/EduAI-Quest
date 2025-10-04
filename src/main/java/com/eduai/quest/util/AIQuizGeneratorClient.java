package com.eduai.quest.util;

import com.eduai.quest.model.dto.quiz.GenerateQuizRequest;
import com.eduai.quest.model.dto.quiz.QuizResponse;

public interface AIQuizGeneratorClient {
    QuizResponse generateQuiz(GenerateQuizRequest request);
}