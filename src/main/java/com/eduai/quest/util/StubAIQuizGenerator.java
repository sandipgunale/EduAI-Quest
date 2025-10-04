package com.eduai.quest.util;

import com.eduai.quest.model.dto.quiz.GenerateQuizRequest;
import com.eduai.quest.model.dto.quiz.QuestionDto;
import com.eduai.quest.model.dto.quiz.QuizResponse;
import com.eduai.quest.model.entity.Question;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
@ConditionalOnProperty(name = "app.ai.quiz-generator.enabled", havingValue = "true", matchIfMissing = true)
@Slf4j
public class StubAIQuizGenerator implements AIQuizGeneratorClient {

    @Override
    public QuizResponse generateQuiz(GenerateQuizRequest request) {
        log.info("Generating quiz with AI stub for topic: {}", request.getTopic());

        // Simulate AI processing delay
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        QuizResponse response = new QuizResponse();
        response.setTitle(request.getTopic() + " - AI Generated Quiz");
        response.setDifficulty(request.getDifficulty());
        response.setGeneratedByAI(true);

        // Generate sample questions based on the topic
        List<QuestionDto> questions = generateSampleQuestions(request);
        response.setQuestions(questions);

        return response;
    }

    private List<QuestionDto> generateSampleQuestions(GenerateQuizRequest request) {
        if (request.getTopic().toLowerCase().contains("java")) {
            return generateJavaQuestions(request.getQuestionCount());
        } else if (request.getTopic().toLowerCase().contains("python")) {
            return generatePythonQuestions(request.getQuestionCount());
        } else {
            return generateGeneralQuestions(request.getQuestionCount());
        }
    }

    private List<QuestionDto> generateJavaQuestions(int count) {
        return Arrays.asList(
                createMCQQuestion("Which keyword enables runtime polymorphism in Java?",
                        Arrays.asList("final", "static", "extends", "instanceof"), "extends"),
                createTrueFalseQuestion("Java supports multiple inheritance for classes. (True/False)", "False"),
                createMCQQuestion("What is the default value of a boolean variable in Java?",
                        Arrays.asList("true", "false", "null", "0"), "false")
        ).subList(0, Math.min(count, 3));
    }

    private List<QuestionDto> generatePythonQuestions(int count) {
        return Arrays.asList(
                createMCQQuestion("Which of the following is used to define a function in Python?",
                        Arrays.asList("function", "def", "define", "func"), "def"),
                createTrueFalseQuestion("Python lists are mutable. (True/False)", "True"),
                createMCQQuestion("What does PEP 8 refer to in Python?",
                        Arrays.asList("Python Enhancement Proposal", "Python Error Protocol",
                                "Program Execution Process", "Package Extension Process"), "Python Enhancement Proposal")
        ).subList(0, Math.min(count, 3));
    }

    private List<QuestionDto> generateGeneralQuestions(int count) {
        return Arrays.asList(
                createMCQQuestion("What does API stand for?",
                        Arrays.asList("Application Programming Interface", "Advanced Programming Interface",
                                "Application Process Integration", "Automated Programming Interface"),
                        "Application Programming Interface"),
                createTrueFalseQuestion("HTTP is a stateless protocol. (True/False)", "True")
        ).subList(0, Math.min(count, 2));
    }

    private QuestionDto createMCQQuestion(String text, List<String> options, String correctOption) {
        QuestionDto question = new QuestionDto();
        question.setText(text);
        question.setType(Question.QuestionType.MCQ);
        question.setOptions(options);
        question.setCorrectOption(correctOption);
        return question;
    }

    private QuestionDto createTrueFalseQuestion(String text, String correctOption) {
        QuestionDto question = new QuestionDto();
        question.setText(text);
        question.setType(Question.QuestionType.TRUE_FALSE);
        question.setOptions(Arrays.asList("True", "False"));
        question.setCorrectOption(correctOption);
        return question;
    }
}