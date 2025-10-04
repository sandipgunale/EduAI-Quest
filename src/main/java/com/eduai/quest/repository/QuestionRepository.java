package com.eduai.quest.repository;

import com.eduai.quest.model.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long> {

    // Custom query for MySQL JSON operations
    @Query(value = "SELECT * FROM questions q WHERE JSON_CONTAINS(q.options_json, :option, '$')", nativeQuery = true)
    List<Question> findByOptionContaining(@Param("option") String option);

    List<Question> findByQuizId(Long quizId);
}