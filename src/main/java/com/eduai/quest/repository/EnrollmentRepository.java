package com.eduai.quest.repository;

import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.Enrollment;
import com.eduai.quest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    boolean existsByCourseAndUser(Course course, User user);
    Optional<Enrollment> findByCourseAndUser(Course course, User user);
    List<Enrollment> findByUser(User user);
    List<Enrollment> findByCourse(Course course);
}