package com.eduai.quest.service.impl;

import com.eduai.quest.model.entity.User;
import com.eduai.quest.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @PostConstruct
    @Transactional
    public void initializeData() {
        try {
            log.info("🔧 Starting data initialization...");

            // Check if admin user exists
            if (userRepository.findByEmail("admin@eduai.com").isEmpty()) {
                createDefaultUsers();
                log.info("✅ Default users created successfully");
            } else {
                log.info("ℹ️ Default users already exist, skipping initialization");
            }

            log.info("✅ Data initialization completed");
        } catch (Exception e) {
            log.error("❌ Failed to initialize default data: {}", e.getMessage());
            log.error("This might be normal if the database is not ready yet");
        }
    }

    private void createDefaultUsers() {
        // Create admin user - password: admin123
        User admin = User.builder()
                .email("admin@eduai.com")
                .name("Admin User")
                .password(passwordEncoder.encode("admin123"))
                .roles(new HashSet<>())
                .enabled(true)
                .build();
        admin.getRoles().add("ADMIN");
        admin.getRoles().add("TEACHER");
        userRepository.save(admin);

        // Create teacher user - password: teacher123
        User teacher = User.builder()
                .email("teacher@eduai.com")
                .name("Teacher User")
                .password(passwordEncoder.encode("teacher123"))
                .roles(new HashSet<>())
                .enabled(true)
                .build();
        teacher.getRoles().add("TEACHER");
        userRepository.save(teacher);

        // Create student user - password: student123
        User student = User.builder()
                .email("student@eduai.com")
                .name("Student User")
                .password(passwordEncoder.encode("student123"))
                .roles(new HashSet<>())
                .enabled(true)
                .build();
        student.getRoles().add("STUDENT");
        userRepository.save(student);

        log.info("👥 Created default users:");
        log.info("   - Admin: admin@eduai.com / admin123");
        log.info("   - Teacher: teacher@eduai.com / teacher123");
        log.info("   - Student: student@eduai.com / student123");
    }
}