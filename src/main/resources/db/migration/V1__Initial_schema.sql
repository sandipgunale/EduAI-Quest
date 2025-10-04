-- Create database (run this manually first)
-- CREATE DATABASE IF NOT EXISTS eduai_quest CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE eduai_quest;

-- Users table
CREATE TABLE users (
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
CREATE TABLE user_roles (
                            user_id BIGINT NOT NULL,
                            role VARCHAR(20) NOT NULL,
                            PRIMARY KEY (user_id, role),
                            FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                            INDEX idx_user_roles_user_id (user_id)
);

-- Courses table
CREATE TABLE courses (
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
CREATE TABLE modules (
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
CREATE TABLE lessons (
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
CREATE TABLE enrollments (
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
CREATE TABLE quizzes (
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
CREATE TABLE questions (
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
CREATE TABLE quiz_attempts (
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