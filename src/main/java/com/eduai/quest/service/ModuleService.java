package com.eduai.quest.service;

import com.eduai.quest.model.dto.course.ModuleRequest;
import com.eduai.quest.model.dto.course.ModuleResponse;

import java.util.List;

public interface ModuleService {
    ModuleResponse createModule(Long courseId, ModuleRequest request);
    ModuleResponse updateModule(Long id, ModuleRequest request);
    ModuleResponse getModuleById(Long id);
    List<ModuleResponse> getModulesByCourse(Long courseId);
    void deleteModule(Long id);
}