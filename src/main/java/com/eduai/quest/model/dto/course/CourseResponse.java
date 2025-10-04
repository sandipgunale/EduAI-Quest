package com.eduai.quest.model.dto.course;

import com.eduai.quest.model.entity.Course;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CourseResponse {
    private Long id;
    private String title;
    private String description;
    private String category;
    private Course.Difficulty difficulty;
    private String createdBy;
    private LocalDateTime createdAt;
    private Integer moduleCount;
    private Integer enrollmentCount;
}