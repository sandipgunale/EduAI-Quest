package com.eduai.quest.model.dto.forum;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class ForumCommentResponse {
    private Long id;
    private String content;
    private String authorName;
    private String authorEmail;
    private Long postId;
    private Long parentCommentId;
    private List<ForumCommentResponse> replies;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}