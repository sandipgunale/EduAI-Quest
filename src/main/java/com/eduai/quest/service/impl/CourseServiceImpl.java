package com.eduai.quest.service.impl;

import com.eduai.quest.exception.ResourceNotFoundException;
import com.eduai.quest.model.dto.course.CourseRequest;
import com.eduai.quest.model.dto.course.CourseResponse;
import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.Enrollment;
import com.eduai.quest.model.entity.User;
import com.eduai.quest.repository.CourseRepository;
import com.eduai.quest.repository.EnrollmentRepository;
import com.eduai.quest.service.CourseService;
import com.eduai.quest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final EnrollmentRepository enrollmentRepository;
    private final UserService userService;

    @Override
    @Transactional
    public CourseResponse createCourse(CourseRequest courseRequest) {
        User currentUser = userService.getCurrentUser();

        Course course = new Course();
        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setCategory(courseRequest.getCategory());
        course.setDifficulty(courseRequest.getDifficulty());
        course.setCreatedBy(currentUser);

        Course savedCourse = courseRepository.save(course);
        return mapToCourseResponse(savedCourse);
    }

    @Override
    @Transactional
    public CourseResponse updateCourse(Long id, CourseRequest courseRequest) {
        Course course = getCourseEntity(id);

        course.setTitle(courseRequest.getTitle());
        course.setDescription(courseRequest.getDescription());
        course.setCategory(courseRequest.getCategory());
        course.setDifficulty(courseRequest.getDifficulty());

        Course updatedCourse = courseRepository.save(course);
        return mapToCourseResponse(updatedCourse);
    }

    @Override
    @Transactional(readOnly = true)
    public CourseResponse getCourseById(Long id) {
        Course course = getCourseEntity(id);
        return mapToCourseResponse(course);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<CourseResponse> getAllCourses(Pageable pageable, String category, Course.Difficulty difficulty) {
        Specification<Course> spec = Specification.where(null);

        if (category != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("category"), category));
        }

        if (difficulty != null) {
            spec = spec.and((root, query, cb) -> cb.equal(root.get("difficulty"), difficulty));
        }

        return courseRepository.findAll(spec, pageable)
                .map(this::mapToCourseResponse);
    }

    @Override
    @Transactional
    public void deleteCourse(Long id) {
        Course course = getCourseEntity(id);
        courseRepository.delete(course);
    }

    @Override
    @Transactional
    public void enrollUserInCourse(Long courseId, Long userId) {
        Course course = getCourseEntity(courseId);
        User user = userService.getUserById(userId);

        // Check if already enrolled
        boolean alreadyEnrolled = enrollmentRepository.existsByCourseAndUser(course, user);
        if (alreadyEnrolled) {
            throw new IllegalArgumentException("User is already enrolled in this course");
        }

        Enrollment enrollment = Enrollment.builder()
                .course(course)
                .user(user)
                .enrolledAt(LocalDateTime.now())
                .status(Enrollment.Status.ACTIVE)
                .build();

        enrollmentRepository.save(enrollment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CourseResponse> searchCourses(String query) {
        List<Course> courses = courseRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        return courses.stream()
                .map(this::mapToCourseResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Course getCourseEntity(Long id) {
        return courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
    }

    private CourseResponse mapToCourseResponse(Course course) {
        CourseResponse response = new CourseResponse();
        response.setId(course.getId());
        response.setTitle(course.getTitle());
        response.setDescription(course.getDescription());
        response.setCategory(course.getCategory());
        response.setDifficulty(course.getDifficulty());
        response.setCreatedBy(course.getCreatedBy().getName());
        response.setCreatedAt(course.getCreatedAt());
        response.setModuleCount(course.getModules().size());
        response.setEnrollmentCount(course.getEnrollments().size());
        return response;
    }
}