package com.eduai.quest.model.dto.forum;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ForumCommentRequest {
    @NotBlank
    private String content;

    private Long parentCommentId;
}