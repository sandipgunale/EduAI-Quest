package com.eduai.quest.controller;

import com.eduai.quest.model.dto.quiz.GenerateQuizRequest;
import com.eduai.quest.model.dto.quiz.QuizResponse;
import com.eduai.quest.service.QuizService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/quizzes")
@RequiredArgsConstructor
@Tag(name = "Quizzes", description = "Quiz management and AI generation APIs")
public class QuizController {

    private final QuizService quizService;

    @PostMapping("/generate")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Generate a quiz using AI")
    public ResponseEntity<QuizResponse> generateQuiz(@Valid @RequestBody GenerateQuizRequest request) {
        QuizResponse response = quizService.generateQuiz(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/course/{courseId}")
    @Operation(summary = "Get quizzes for a course")
    public ResponseEntity<List<QuizResponse>> getCourseQuizzes(@PathVariable Long courseId) {
        List<QuizResponse> quizzes = quizService.getQuizzesByCourse(courseId);
        return ResponseEntity.ok(quizzes);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get quiz by ID")
    public ResponseEntity<QuizResponse> getQuiz(@PathVariable Long id) {
        QuizResponse quiz = quizService.getQuizById(id);
        return ResponseEntity.ok(quiz);
    }
}