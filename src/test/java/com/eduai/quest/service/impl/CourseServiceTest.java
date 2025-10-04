package com.eduai.quest.service.impl;

import com.eduai.quest.dto.course.CourseRequest;
import com.eduai.quest.dto.course.CourseResponse;
import com.eduai.quest.exception.ResourceNotFoundException;
import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.User;
import com.eduai.quest.repository.CourseRepository;
import com.eduai.quest.repository.EnrollmentRepository;
import com.eduai.quest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private UserService userService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course testCourse;
    private User testUser;
    private CourseRequest courseRequest;
    private CourseResponse courseResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .email("teacher@example.com")
                .name("Test Teacher")
                .build();

        testCourse = Course.builder()
                .id(1L)
                .title("Test Course")
                .description("Test Description")
                .category("Programming")
                .difficulty(Course.Difficulty.BEGINNER)
                .createdBy(testUser)
                .build();

        courseRequest = new CourseRequest();
        courseRequest.setTitle("Test Course");
        courseRequest.setDescription("Test Description");
        courseRequest.setCategory("Programming");
        courseRequest.setDifficulty(Course.Difficulty.BEGINNER);

        courseResponse = new CourseResponse();
        courseResponse.setId(1L);
        courseResponse.setTitle("Test Course");
        courseResponse.setDescription("Test Description");
        courseResponse.setCategory("Programming");
        courseResponse.setDifficulty(Course.Difficulty.BEGINNER);
    }

    @Test
    void createCourse_ShouldReturnCourseResponse() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(modelMapper.map(courseRequest, Course.class)).thenReturn(testCourse);
        when(courseRepository.save(any(Course.class))).thenReturn(testCourse);
        when(modelMapper.map(testCourse, CourseResponse.class)).thenReturn(courseResponse);

        CourseResponse result = courseService.createCourse(courseRequest);

        assertNotNull(result);
        assertEquals(courseResponse.getTitle(), result.getTitle());
        verify(courseRepository, times(1)).save(testCourse);
    }

    @Test
    void getCourseById_WhenCourseExists_ShouldReturnCourseResponse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(modelMapper.map(testCourse, CourseResponse.class)).thenReturn(courseResponse);

        CourseResponse result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals(courseResponse.getId(), result.getId());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getCourseById_WhenCourseNotExists_ShouldThrowException() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> courseService.getCourseById(1L));
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void getAllCourses_ShouldReturnPageOfCourses() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<Course> coursePage = new PageImpl<>(Arrays.asList(testCourse));

        when(courseRepository.findAll(any(Specification.class), eq(pageable))).thenReturn(coursePage);
        when(modelMapper.map(testCourse, CourseResponse.class)).thenReturn(courseResponse);

        Page<CourseResponse> result = courseService.getAllCourses(pageable, null, null);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        verify(courseRepository, times(1)).findAll(any(Specification.class), eq(pageable));
    }

    @Test
    void deleteCourse_WhenCourseExists_ShouldDeleteCourse() {
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        doNothing().when(courseRepository).delete(testCourse);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).findById(1L);
        verify(courseRepository, times(1)).delete(testCourse);
    }
}