package com.eduai.quest.repository;

import com.eduai.quest.model.entity.Lesson;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findByModuleIdOrderByOrderIndex(Long moduleId);
    long countByModuleCourseId(Long courseId);
    List<Lesson> findByModuleCourseId(Long courseId);
}