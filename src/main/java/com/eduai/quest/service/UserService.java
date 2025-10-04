package com.eduai.quest.service;

import com.eduai.quest.model.dto.course.CourseResponse;
import com.eduai.quest.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    User createUser(User user);
    User updateUser(Long id, User userDetails);
    User getUserById(Long id);
    User getUserByEmail(String email);
    Page<User> getAllUsers(Pageable pageable);
    void deleteUser(Long id);
    List<CourseResponse> getUserEnrolledCourses(Long userId);
    User getCurrentUser();
}