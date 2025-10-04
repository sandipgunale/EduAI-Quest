package com.eduai.quest.service;

import com.eduai.quest.model.dto.progress.ProgressResponse;
import com.eduai.quest.model.entity.UserProgress;

public interface ProgressService {
    ProgressResponse trackLessonProgress(Long userId, Long lessonId, Double progress);
    ProgressResponse getCourseProgress(Long userId, Long courseId);
    UserProgress markLessonCompleted(Long userId, Long lessonId);
    ProgressResponse calculateOverallProgress(Long userId);
}