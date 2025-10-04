USE eduai_quest;

-- Insert sample users
INSERT INTO users (email, name, password, enabled) VALUES
                                                       ('admin@eduai.com', 'Admin User', '$2a$10$xyz123', TRUE),
                                                       ('teacher@eduai.com', 'Teacher User', '$2a$10$xyz123', TRUE),
                                                       ('student@eduai.com', 'Student User', '$2a$10$xyz123', TRUE);

-- Insert user roles
INSERT INTO user_roles (user_id, role) VALUES
                                           (1, 'ADMIN'),
                                           (1, 'TEACHER'),
                                           (2, 'TEACHER'),
                                           (3, 'STUDENT');

-- Insert sample courses
INSERT INTO courses (title, description, category, difficulty, created_by) VALUES
                                                                               ('Java Programming Basics', 'Learn the fundamentals of Java programming', 'Programming', 'BEGINNER', 2),
                                                                               ('Advanced Python', 'Master advanced Python concepts and frameworks', 'Programming', 'ADVANCED', 2),
                                                                               ('Web Development Fundamentals', 'Learn HTML, CSS, and JavaScript', 'Web Development', 'BEGINNER', 2);

-- Insert sample modules
INSERT INTO modules (title, description, order_index, course_id) VALUES
                                                                     ('Introduction to Java', 'Get started with Java programming', 1, 1),
                                                                     ('Object-Oriented Programming', 'Learn OOP concepts in Java', 2, 1),
                                                                     ('Python Data Structures', 'Advanced data structures in Python', 1, 2);

-- Insert sample lessons
INSERT INTO lessons (title, content, order_index, module_id) VALUES
                                                                 ('Java Syntax Basics', 'Learn basic Java syntax and structure', 1, 1),
                                                                 ('Variables and Data Types', 'Understanding variables and data types in Java', 2, 1),
                                                                 ('Classes and Objects', 'Introduction to classes and objects', 1, 2);

-- Insert sample enrollments
INSERT INTO enrollments (user_id, course_id, status) VALUES
                                                         (3, 1, 'ACTIVE'),
                                                         (3, 2, 'ACTIVE');