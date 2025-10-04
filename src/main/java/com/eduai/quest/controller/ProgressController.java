package com.eduai.quest.controller;

import com.eduai.quest.model.dto.progress.ProgressResponse;
import com.eduai.quest.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/progress")
@RequiredArgsConstructor
@Tag(name = "Progress", description = "Learning progress tracking APIs")
public class ProgressController {

    private final ProgressService progressService;

    @PostMapping("/lessons/{lessonId}")
    @Operation(summary = "Track lesson progress")
    public ResponseEntity<ProgressResponse> trackLessonProgress(
            @PathVariable Long lessonId,
            @RequestParam Double progress) {
        // In real implementation, get user ID from security context
        Long userId = 1L; // Placeholder
        ProgressResponse response = progressService.trackLessonProgress(userId, lessonId, progress);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/courses/{courseId}")
    @Operation(summary = "Get course progress")
    public ResponseEntity<ProgressResponse> getCourseProgress(@PathVariable Long courseId) {
        // In real implementation, get user ID from security context
        Long userId = 1L; // Placeholder
        ProgressResponse response = progressService.getCourseProgress(userId, courseId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/lessons/{lessonId}/complete")
    @Operation(summary = "Mark lesson as completed")
    public ResponseEntity<ProgressResponse> markLessonCompleted(@PathVariable Long lessonId) {
        // In real implementation, get user ID from security context
        Long userId = 1L; // Placeholder
        progressService.markLessonCompleted(userId, lessonId);
        ProgressResponse response = progressService.trackLessonProgress(userId, lessonId, 100.0);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/overall")
    @Operation(summary = "Get overall learning progress")
    public ResponseEntity<ProgressResponse> getOverallProgress() {
        // In real implementation, get user ID from security context
        Long userId = 1L; // Placeholder
        ProgressResponse response = progressService.calculateOverallProgress(userId);
        return ResponseEntity.ok(response);
    }
}