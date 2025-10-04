package com.eduai.quest.service.impl;

import com.eduai.quest.model.dto.course.ModuleRequest;
import com.eduai.quest.model.dto.course.ModuleResponse;
import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.Module;
import com.eduai.quest.repository.CourseRepository;
import com.eduai.quest.repository.ModuleRepository;
import com.eduai.quest.service.ModuleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ModuleServiceImpl implements ModuleService {

    private final ModuleRepository moduleRepository;
    private final CourseRepository courseRepository;

    @Override
    @Transactional
    public ModuleResponse createModule(Long courseId, ModuleRequest request) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Module module = new Module();
        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setOrderIndex(request.getOrderIndex());
        module.setCourse(course);

        Module savedModule = moduleRepository.save(module);
        return convertToResponse(savedModule);
    }

    @Override
    @Transactional
    public ModuleResponse updateModule(Long id, ModuleRequest request) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        module.setTitle(request.getTitle());
        module.setDescription(request.getDescription());
        module.setOrderIndex(request.getOrderIndex());

        Module updatedModule = moduleRepository.save(module);
        return convertToResponse(updatedModule);
    }

    @Override
    @Transactional(readOnly = true)
    public ModuleResponse getModuleById(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        return convertToResponse(module);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ModuleResponse> getModulesByCourse(Long courseId) {
        List<Module> modules = moduleRepository.findByCourseIdOrderByOrderIndex(courseId);
        return modules.stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void deleteModule(Long id) {
        Module module = moduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        moduleRepository.delete(module);
    }

    private ModuleResponse convertToResponse(Module module) {
        ModuleResponse response = new ModuleResponse();
        response.setId(module.getId());
        response.setTitle(module.getTitle());
        response.setDescription(module.getDescription());
        response.setOrderIndex(module.getOrderIndex());
        response.setCourseId(module.getCourse().getId());
        response.setCreatedAt(module.getCreatedAt());
        return response;
    }
}