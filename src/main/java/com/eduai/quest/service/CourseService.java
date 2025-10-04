package com.eduai.quest.service;

import com.eduai.quest.model.dto.course.CourseRequest;
import com.eduai.quest.model.dto.course.CourseResponse;
import com.eduai.quest.model.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    CourseResponse createCourse(CourseRequest courseRequest);
    CourseResponse updateCourse(Long id, CourseRequest courseRequest);
    CourseResponse getCourseById(Long id);
    Page<CourseResponse> getAllCourses(Pageable pageable, String category, Course.Difficulty difficulty);
    void deleteCourse(Long id);
    void enrollUserInCourse(Long courseId, Long userId);
    List<CourseResponse> searchCourses(String query);
    Course getCourseEntity(Long id);
}