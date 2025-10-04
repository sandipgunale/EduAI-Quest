package com.eduai.quest.model.dto.progress;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProgressResponse {
    private Double progressPercentage;
    private String status;
    private Long itemId;
    private String itemType;
    private Long completedItems;
    private Long totalItems;
    private LocalDateTime lastUpdated;
}