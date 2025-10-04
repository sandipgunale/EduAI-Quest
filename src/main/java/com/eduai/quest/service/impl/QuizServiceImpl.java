package com.eduai.quest.service.impl;

import com.eduai.quest.model.dto.quiz.GenerateQuizRequest;
import com.eduai.quest.model.dto.quiz.QuestionDto;
import com.eduai.quest.model.dto.quiz.QuizResponse;
import com.eduai.quest.model.entity.*;
import com.eduai.quest.model.entity.Module;
import com.eduai.quest.repository.CourseRepository;
import com.eduai.quest.repository.ModuleRepository;
import com.eduai.quest.repository.QuizRepository;
import com.eduai.quest.service.QuizService;
import com.eduai.quest.util.AIQuizGeneratorClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final CourseRepository courseRepository;
    private final ModuleRepository moduleRepository;
    private final AIQuizGeneratorClient aiQuizGeneratorClient;

    @Override
    @Transactional
    public QuizResponse generateQuiz(GenerateQuizRequest request) {
        // Use AI to generate quiz
        QuizResponse aiGeneratedQuiz = aiQuizGeneratorClient.generateQuiz(request);

        if (request.getSave() != null && request.getSave()) {
            // Save the generated quiz to database
            Quiz quiz = new Quiz();
            quiz.setTitle(aiGeneratedQuiz.getTitle());
            quiz.setDifficulty(request.getDifficulty());
            quiz.setGeneratedByAI(true);

            // Set course if provided
            if (request.getCourseId() != null) {
                Course course = courseRepository.findById(request.getCourseId())
                        .orElseThrow(() -> new RuntimeException("Course not found"));
                quiz.setCourse(course);
            }

            // Set module if provided
            if (request.getModuleId() != null) {
                Module module = moduleRepository.findById(request.getModuleId())
                        .orElseThrow(() -> new RuntimeException("Module not found"));
                quiz.setModule(module);
            }

            // Convert and save questions
            List<Question> questions = aiGeneratedQuiz.getQuestions().stream()
                    .map(qDto -> {
                        Question question = new Question();
                        question.setText(qDto.getText());
                        question.setType(qDto.getType());
                        question.setOptions(qDto.getOptions());
                        question.setCorrectOption(qDto.getCorrectOption());
                        question.setQuiz(quiz);
                        return question;
                    })
                    .collect(Collectors.toList());

            quiz.setQuestions(questions);
            Quiz savedQuiz = quizRepository.save(quiz);

            return convertToResponse(savedQuiz);
        }

        return aiGeneratedQuiz;
    }

    @Override
    @Transactional(readOnly = true)
    public List<QuizResponse> getQuizzesByCourse(Long courseId) {
        List<Quiz> quizzes = quizRepository.findByCourseId(courseId);
        return quizzes.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public QuizResponse getQuizById(Long id) {
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Quiz not found"));
        return convertToResponse(quiz);
    }

    private QuizResponse convertToResponse(Quiz quiz) {
        QuizResponse response = new QuizResponse();
        response.setId(quiz.getId());
        response.setTitle(quiz.getTitle());
        response.setDifficulty(quiz.getDifficulty());
        response.setGeneratedByAI(quiz.isGeneratedByAI());

        if (quiz.getCourse() != null) {
            response.setCourseId(quiz.getCourse().getId());
        }
        if (quiz.getModule() != null) {
            response.setModuleId(quiz.getModule().getId());
        }

        List<QuestionDto> questionDtos = quiz.getQuestions().stream()
                .map(q -> {
                    QuestionDto dto = new QuestionDto();
                    dto.setId(q.getId());
                    dto.setText(q.getText());
                    dto.setType(q.getType());
                    dto.setOptions(q.getOptions());
                    dto.setCorrectOption(q.getCorrectOption());
                    return dto;
                })
                .collect(Collectors.toList());

        response.setQuestions(questionDtos);
        return response;
    }
}