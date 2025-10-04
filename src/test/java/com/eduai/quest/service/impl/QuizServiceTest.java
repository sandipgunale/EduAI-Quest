package com.eduai.quest.service.impl;

import com.eduai.quest.model.dto.quiz.GenerateQuizRequest;
import com.eduai.quest.model.dto.quiz.QuizResponse;
import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.Quiz;
import com.eduai.quest.repository.CourseRepository;
import com.eduai.quest.repository.QuizRepository;
import com.eduai.quest.util.AIQuizGeneratorClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuizServiceTest {

    @Mock
    private QuizRepository quizRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private AIQuizGeneratorClient aiQuizGeneratorClient;

    @InjectMocks
    private QuizServiceImpl quizService;

    private GenerateQuizRequest generateRequest;
    private QuizResponse aiQuizResponse;
    private Course testCourse;
    private Quiz testQuiz;

    @BeforeEach
    void setUp() {
        generateRequest = new GenerateQuizRequest();
        generateRequest.setTopic("Java Programming");
        generateRequest.setQuestionCount(5);
        generateRequest.setSave(true);
        generateRequest.setCourseId(1L);

        aiQuizResponse = new QuizResponse();
        aiQuizResponse.setTitle("Java Programming - AI Generated Quiz");

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Java Basics");

        testQuiz = new Quiz();
        testQuiz.setId(1L);
        testQuiz.setTitle("Java Programming - AI Generated Quiz");
    }

    @Test
    void generateQuiz_ShouldReturnQuizResponse() {
        when(aiQuizGeneratorClient.generateQuiz(generateRequest)).thenReturn(aiQuizResponse);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(quizRepository.save(any(Quiz.class))).thenReturn(testQuiz);

        QuizResponse response = quizService.generateQuiz(generateRequest);

        assertNotNull(response);
        assertEquals("Java Programming - AI Generated Quiz", response.getTitle());
        verify(quizRepository, times(1)).save(any(Quiz.class));
    }
}