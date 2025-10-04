package com.eduai.quest.service.impl;

import com.eduai.quest.exception.ResourceNotFoundException;
import com.eduai.quest.model.entity.User;
import com.eduai.quest.repository.UserRepository;
import com.eduai.quest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    @Transactional
    public User createUser(User user) {
        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);

        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }
        if (userDetails.getEmail() != null) {
            user.setEmail(userDetails.getEmail());
        }
        if (userDetails.getRoles() != null) {
            user.setRoles(userDetails.getRoles());
        }

        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }

    @Override
    @Transactional(readOnly = true)
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = getUserById(id);
        userRepository.delete(user);
    }

    @Override
    @Transactional(readOnly = true)
    public java.util.List<com.eduai.quest.model.dto.course.CourseResponse> getUserEnrolledCourses(Long userId) {
        User user = getUserById(userId);
        return user.getEnrollments().stream()
                .map(enrollment -> {
                    com.eduai.quest.model.dto.course.CourseResponse response = new com.eduai.quest.model.dto.course.CourseResponse();
                    response.setId(enrollment.getCourse().getId());
                    response.setTitle(enrollment.getCourse().getTitle());
                    response.setDescription(enrollment.getCourse().getDescription());
                    response.setCategory(enrollment.getCourse().getCategory());
                    response.setDifficulty(enrollment.getCourse().getDifficulty());
                    response.setCreatedBy(enrollment.getCourse().getCreatedBy().getName());
                    response.setCreatedAt(enrollment.getCourse().getCreatedAt());
                    response.setModuleCount(enrollment.getCourse().getModules().size());
                    response.setEnrollmentCount(enrollment.getCourse().getEnrollments().size());
                    return response;
                })
                .collect(java.util.stream.Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public User getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return getUserByEmail(email);
    }
}