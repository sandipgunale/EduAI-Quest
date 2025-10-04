package com.eduai.quest.repository;

import com.eduai.quest.model.entity.Quiz;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuizRepository extends JpaRepository<Quiz, Long> {
    List<Quiz> findByCourseId(Long courseId);
    List<Quiz> findByModuleId(Long moduleId);
    List<Quiz> findByGeneratedByAI(boolean generatedByAI);
}