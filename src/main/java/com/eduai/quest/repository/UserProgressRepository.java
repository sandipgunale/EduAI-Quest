package com.eduai.quest.repository;

import com.eduai.quest.model.entity.UserProgress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserProgressRepository extends JpaRepository<UserProgress, Long> {
    Optional<UserProgress> findByUserIdAndLessonId(Long userId, Long lessonId);
    List<UserProgress> findByUserIdAndCourseId(Long userId, Long courseId);
    List<UserProgress> findByUserIdAndModuleId(Long userId, Long moduleId);

    @Query("SELECT up FROM UserProgress up WHERE up.user.id = :userId AND up.course.id = :courseId AND up.lesson IS NULL")
    Optional<UserProgress> findCourseProgress(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT COUNT(up) FROM UserProgress up WHERE up.user.id = :userId AND up.course.id = :courseId AND up.status = 'COMPLETED'")
    long countCompletedLessons(@Param("userId") Long userId, @Param("courseId") Long courseId);

    @Query("SELECT AVG(up.progressPercentage) FROM UserProgress up WHERE up.user.id = :userId AND up.course.id = :courseId")
    Double calculateCourseProgress(@Param("userId") Long userId, @Param("courseId") Long courseId);
}