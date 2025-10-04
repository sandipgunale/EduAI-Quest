USE eduai_quest;

-- Forum posts table
CREATE TABLE forum_posts (
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
CREATE TABLE forum_comments (
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
CREATE TABLE user_progress (
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