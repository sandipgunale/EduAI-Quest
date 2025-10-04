-- Create database if it doesn't exist
CREATE DATABASE IF NOT EXISTS eduai_quest CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE eduai_quest;

-- Users table
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     email VARCHAR(255) NOT NULL UNIQUE,
                                     name VARCHAR(100) NOT NULL,
                                     password VARCHAR(255),
                                     google_id VARCHAR(100),
                                     enabled BOOLEAN DEFAULT TRUE,
                                     created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                     updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                     INDEX idx_user_email (email),
                                     INDEX idx_user_google_id (google_id)
);

-- User roles table
CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id BIGINT NOT NULL,
                                          role VARCHAR(20) NOT NULL,
                                          PRIMARY KEY (user_id, role),
                                          FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                          INDEX idx_user_roles_user_id (user_id)
);

-- Courses table
CREATE TABLE IF NOT EXISTS courses (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title VARCHAR(255) NOT NULL,
                                       description TEXT,
                                       category VARCHAR(100) NOT NULL,
                                       difficulty ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED') NOT NULL,
                                       created_by BIGINT,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (created_by) REFERENCES users(id),
                                       INDEX idx_course_title (title),
                                       INDEX idx_course_category (category)
);

-- Modules table
CREATE TABLE IF NOT EXISTS modules (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title VARCHAR(255) NOT NULL,
                                       description TEXT,
                                       order_index INT,
                                       course_id BIGINT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
                                       INDEX idx_module_course (course_id)
);

-- Lessons table
CREATE TABLE IF NOT EXISTS lessons (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title VARCHAR(255) NOT NULL,
                                       content TEXT,
                                       video_url VARCHAR(500),
                                       file_path VARCHAR(500),
                                       order_index INT,
                                       module_id BIGINT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE CASCADE,
                                       INDEX idx_lesson_module (module_id)
);

-- Enrollments table
CREATE TABLE IF NOT EXISTS enrollments (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           user_id BIGINT NOT NULL,
                                           course_id BIGINT NOT NULL,
                                           status ENUM('ACTIVE', 'COMPLETED', 'DROPPED') NOT NULL DEFAULT 'ACTIVE',
                                           enrolled_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           completed_at TIMESTAMP NULL,
                                           FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                           FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE CASCADE,
                                           UNIQUE KEY unique_user_course (user_id, course_id),
                                           INDEX idx_enrollment_user (user_id),
                                           INDEX idx_enrollment_course (course_id)
);

-- Quizzes table
CREATE TABLE IF NOT EXISTS quizzes (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       title VARCHAR(255) NOT NULL,
                                       difficulty ENUM('BEGINNER', 'INTERMEDIATE', 'ADVANCED'),
                                       course_id BIGINT,
                                       module_id BIGINT,
                                       generated_by_ai BOOLEAN DEFAULT FALSE,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                       FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
                                       FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE SET NULL,
                                       INDEX idx_quiz_course (course_id)
);

-- Questions table
CREATE TABLE IF NOT EXISTS questions (
                                         id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                         text TEXT NOT NULL,
                                         type ENUM('MCQ', 'TRUE_FALSE', 'SHORT_ANSWER') NOT NULL,
                                         options_json JSON,
                                         correct_option VARCHAR(500) NOT NULL,
                                         quiz_id BIGINT NOT NULL,
                                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                         FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
                                         INDEX idx_question_quiz (quiz_id)
);

-- Quiz attempts table
CREATE TABLE IF NOT EXISTS quiz_attempts (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             user_id BIGINT NOT NULL,
                                             quiz_id BIGINT NOT NULL,
                                             score INT,
                                             total_questions INT,
                                             attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             completed_at TIMESTAMP NULL,
                                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                             FOREIGN KEY (quiz_id) REFERENCES quizzes(id) ON DELETE CASCADE,
                                             INDEX idx_attempt_user (user_id),
                                             INDEX idx_attempt_quiz (quiz_id)
);

-- Forum posts table
CREATE TABLE IF NOT EXISTS forum_posts (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           title VARCHAR(255) NOT NULL,
                                           content TEXT NOT NULL,
                                           author_id BIGINT NOT NULL,
                                           course_id BIGINT,
                                           pinned BOOLEAN DEFAULT FALSE,
                                           closed BOOLEAN DEFAULT FALSE,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
                                           FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
                                           INDEX idx_forum_post_course (course_id),
                                           INDEX idx_forum_post_author (author_id)
);

-- Forum comments table
CREATE TABLE IF NOT EXISTS forum_comments (
                                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                              content TEXT NOT NULL,
                                              author_id BIGINT NOT NULL,
                                              post_id BIGINT NOT NULL,
                                              parent_comment_id BIGINT,
                                              created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                              updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                              FOREIGN KEY (author_id) REFERENCES users(id) ON DELETE CASCADE,
                                              FOREIGN KEY (post_id) REFERENCES forum_posts(id) ON DELETE CASCADE,
                                              FOREIGN KEY (parent_comment_id) REFERENCES forum_comments(id) ON DELETE CASCADE,
                                              INDEX idx_comment_post (post_id),
                                              INDEX idx_comment_author (author_id),
                                              INDEX idx_comment_parent (parent_comment_id)
);

-- User progress table
CREATE TABLE IF NOT EXISTS user_progress (
                                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                             user_id BIGINT NOT NULL,
                                             course_id BIGINT,
                                             module_id BIGINT,
                                             lesson_id BIGINT,
                                             status ENUM('NOT_STARTED', 'IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'NOT_STARTED',
                                             progress_percentage DOUBLE DEFAULT 0.0,
                                             time_spent_minutes INT DEFAULT 0,
                                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                             completed_at TIMESTAMP NULL,
                                             FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                                             FOREIGN KEY (course_id) REFERENCES courses(id) ON DELETE SET NULL,
                                             FOREIGN KEY (module_id) REFERENCES modules(id) ON DELETE SET NULL,
                                             FOREIGN KEY (lesson_id) REFERENCES lessons(id) ON DELETE SET NULL,
                                             UNIQUE KEY unique_user_lesson (user_id, lesson_id),
                                             INDEX idx_progress_user (user_id),
                                             INDEX idx_progress_course (course_id),
                                             INDEX idx_progress_lesson (lesson_id)
);

-- Insert sample users with properly encoded passwords (BCrypt encoded "password123")
INSERT IGNORE INTO users (id, email, name, password, enabled) VALUES
                                                                  (1, 'admin@eduai.com', 'Admin User', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuDk.1c6XclJ7Iu7cI2eVCvUh6hqgQn.', TRUE),
                                                                  (2, 'teacher@eduai.com', 'Teacher User', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuDk.1c6XclJ7Iu7cI2eVCvUh6hqgQn.', TRUE),
                                                                  (3, 'student@eduai.com', 'Student User', '$2a$10$8.UnVuG9HHgffUDAlk8qfOuDk.1c6XclJ7Iu7cI2eVCvUh6hqgQn.', TRUE);

INSERT IGNORE INTO user_roles (user_id, role) VALUES
                                                  (1, 'ADMIN'),
                                                  (1, 'TEACHER'),
                                                  (2, 'TEACHER'),
                                                  (3, 'STUDENT');

INSERT IGNORE INTO courses (id, title, description, category, difficulty, created_by) VALUES
                                                                                          (1, 'Java Programming Basics', 'Learn the fundamentals of Java programming', 'Programming', 'BEGINNER', 2),
                                                                                          (2, 'Advanced Python', 'Master advanced Python concepts and frameworks', 'Programming', 'ADVANCED', 2),
                                                                                          (3, 'Web Development Fundamentals', 'Learn HTML, CSS, and JavaScript', 'Web Development', 'BEGINNER', 2);

INSERT IGNORE INTO modules (id, title, description, order_index, course_id) VALUES
                                                                                (1, 'Introduction to Java', 'Get started with Java programming', 1, 1),
                                                                                (2, 'Object-Oriented Programming', 'Learn OOP concepts in Java', 2, 1),
                                                                                (3, 'Python Data Structures', 'Advanced data structures in Python', 1, 2);

INSERT IGNORE INTO lessons (id, title, content, order_index, module_id) VALUES
                                                                            (1, 'Java Syntax Basics', 'Learn basic Java syntax and structure', 1, 1),
                                                                            (2, 'Variables and Data Types', 'Understanding variables and data types in Java', 2, 1),
                                                                            (3, 'Classes and Objects', 'Introduction to classes and objects', 1, 2);

INSERT IGNORE INTO enrollments (user_id, course_id, status) VALUES
                                                                (3, 1, 'ACTIVE'),
                                                                (3, 2, 'ACTIVE');

-- Reset auto-increment counters
ALTER TABLE users AUTO_INCREMENT = 100;
ALTER TABLE courses AUTO_INCREMENT = 100;
ALTER TABLE modules AUTO_INCREMENT = 100;
ALTER TABLE lessons AUTO_INCREMENT = 100;