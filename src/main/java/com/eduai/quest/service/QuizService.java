package com.eduai.quest.service;

import com.eduai.quest.model.dto.quiz.GenerateQuizRequest;
import com.eduai.quest.model.dto.quiz.QuizResponse;

import java.util.List;

public interface QuizService {
    QuizResponse generateQuiz(GenerateQuizRequest request);
    List<QuizResponse> getQuizzesByCourse(Long courseId);
    QuizResponse getQuizById(Long id);
}