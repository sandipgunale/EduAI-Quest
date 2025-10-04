package com.eduai.quest.model.dto.forum;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ForumPostResponse {
    private Long id;
    private String title;
    private String content;
    private String authorName;
    private String authorEmail;
    private Long courseId;
    private String courseTitle;
    private Long commentCount;
    private boolean pinned;
    private boolean closed;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}