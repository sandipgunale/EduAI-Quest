package com.eduai.quest.service.impl;

import com.eduai.quest.model.dto.forum.ForumPostRequest;
import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.User;
import com.eduai.quest.repository.CourseRepository;
import com.eduai.quest.repository.ForumPostRepository;
import com.eduai.quest.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ForumServiceTest {

    @Mock
    private ForumPostRepository forumPostRepository;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private UserService userService;

    @InjectMocks
    private ForumServiceImpl forumService;

    private ForumPostRequest postRequest;
    private User testUser;
    private Course testCourse;

    @BeforeEach
    void setUp() {
        postRequest = new ForumPostRequest();
        postRequest.setTitle("Test Post");
        postRequest.setContent("Test Content");
        postRequest.setCourseId(1L);

        testUser = User.builder()
                .id(1L)
                .email("test@example.com")
                .name("Test User")
                .build();

        testCourse = new Course();
        testCourse.setId(1L);
        testCourse.setTitle("Test Course");
    }

    @Test
    void createPost_ShouldReturnPostResponse() {
        when(userService.getCurrentUser()).thenReturn(testUser);
        when(courseRepository.findById(1L)).thenReturn(Optional.of(testCourse));
        when(forumPostRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        var response = forumService.createPost(postRequest);

        assertNotNull(response);
        assertEquals("Test Post", response.getTitle());
        verify(forumPostRepository, times(1)).save(any());
    }
}