package com.eduai.quest.controller;

import com.eduai.quest.model.dto.course.ModuleRequest;
import com.eduai.quest.model.dto.course.ModuleResponse;
import com.eduai.quest.service.ModuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses/{courseId}/modules")
@RequiredArgsConstructor
@Tag(name = "Modules", description = "Module management APIs")
public class ModuleController {

    private final ModuleService moduleService;

    @PostMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Create a new module")
    public ResponseEntity<ModuleResponse> createModule(
            @PathVariable Long courseId,
            @Valid @RequestBody ModuleRequest request) {
        ModuleResponse response = moduleService.createModule(courseId, request);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{moduleId}")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Update a module")
    public ResponseEntity<ModuleResponse> updateModule(
            @PathVariable Long moduleId,
            @Valid @RequestBody ModuleRequest request) {
        ModuleResponse response = moduleService.updateModule(moduleId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{moduleId}")
    @Operation(summary = "Get module by ID")
    public ResponseEntity<ModuleResponse> getModule(@PathVariable Long moduleId) {
        ModuleResponse response = moduleService.getModuleById(moduleId);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Get all modules for a course")
    public ResponseEntity<List<ModuleResponse>> getCourseModules(@PathVariable Long courseId) {
        List<ModuleResponse> modules = moduleService.getModulesByCourse(courseId);
        return ResponseEntity.ok(modules);
    }

    @DeleteMapping("/{moduleId}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Delete a module")
    public ResponseEntity<Void> deleteModule(@PathVariable Long moduleId) {
        moduleService.deleteModule(moduleId);
        return ResponseEntity.ok().build();
    }
}