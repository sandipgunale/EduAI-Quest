package com.eduai.quest.service.impl;

import com.eduai.quest.model.dto.progress.ProgressResponse;
import com.eduai.quest.model.entity.*;
import com.eduai.quest.repository.LessonRepository;
import com.eduai.quest.repository.UserProgressRepository;
import com.eduai.quest.service.ProgressService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgressServiceImpl implements ProgressService {

    private final UserProgressRepository progressRepository;
    private final LessonRepository lessonRepository;

    @Override
    @Transactional
    public ProgressResponse trackLessonProgress(Long userId, Long lessonId, Double progress) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        UserProgress userProgress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> createNewProgress(userId, lesson));

        userProgress.setProgressPercentage(progress);

        if (progress >= 100.0) {
            userProgress.setStatus(UserProgress.ProgressStatus.COMPLETED);
            userProgress.setCompletedAt(LocalDateTime.now());
        } else if (progress > 0) {
            userProgress.setStatus(UserProgress.ProgressStatus.IN_PROGRESS);
        }

        UserProgress savedProgress = progressRepository.save(userProgress);
        return convertToResponse(savedProgress);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressResponse getCourseProgress(Long userId, Long courseId) {
        Double overallProgress = progressRepository.calculateCourseProgress(userId, courseId);
        long completedLessons = progressRepository.countCompletedLessons(userId, courseId);

        ProgressResponse response = new ProgressResponse();
        response.setProgressPercentage(overallProgress != null ? overallProgress : 0.0);
        response.setCompletedItems(completedLessons);
        response.setTotalItems(calculateTotalLessonsInCourse(courseId));
        response.setLastUpdated(LocalDateTime.now());

        return response;
    }

    @Override
    @Transactional
    public UserProgress markLessonCompleted(Long userId, Long lessonId) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new RuntimeException("Lesson not found"));

        UserProgress userProgress = progressRepository.findByUserIdAndLessonId(userId, lessonId)
                .orElseGet(() -> createNewProgress(userId, lesson));

        userProgress.setProgressPercentage(100.0);
        userProgress.setStatus(UserProgress.ProgressStatus.COMPLETED);
        userProgress.setCompletedAt(LocalDateTime.now());

        return progressRepository.save(userProgress);
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressResponse calculateOverallProgress(Long userId) {
        // This would typically calculate progress across all enrolled courses
        // For now, return a simplified version
        ProgressResponse response = new ProgressResponse();
        response.setProgressPercentage(0.0); // Implement actual calculation
        response.setCompletedItems(0L);
        response.setTotalItems(0L);
        response.setLastUpdated(LocalDateTime.now());

        return response;
    }

    private UserProgress createNewProgress(Long userId, Lesson lesson) {
        return UserProgress.builder()
                .user(User.builder().id(userId).build())
                .course(lesson.getModule().getCourse())
                .module(lesson.getModule())
                .lesson(lesson)
                .status(UserProgress.ProgressStatus.IN_PROGRESS)
                .progressPercentage(0.0)
                .timeSpentMinutes(0)
                .build();
    }

    private long calculateTotalLessonsInCourse(Long courseId) {
        // Implement logic to count total lessons in a course
        return lessonRepository.countByModuleCourseId(courseId);
    }

    private ProgressResponse convertToResponse(UserProgress progress) {
        ProgressResponse response = new ProgressResponse();
        response.setProgressPercentage(progress.getProgressPercentage());
        response.setStatus(progress.getStatus().toString());
        response.setLastUpdated(progress.getUpdatedAt());

        if (progress.getLesson() != null) {
            response.setItemId(progress.getLesson().getId());
            response.setItemType("LESSON");
        }

        return response;
    }
}