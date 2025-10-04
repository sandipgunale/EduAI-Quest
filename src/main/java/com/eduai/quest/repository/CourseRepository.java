package com.eduai.quest.repository;

import com.eduai.quest.model.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long>, JpaSpecificationExecutor<Course> {
    List<Course> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);

    @Query("SELECT c FROM Course c WHERE c.createdBy.id = :userId")
    List<Course> findByCreatedBy(@Param("userId") Long userId);

    List<Course> findByCategory(String category);
}