package com.eduai.quest.repository;

import com.eduai.quest.model.entity.Module;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long> {
    List<Module> findByCourseIdOrderByOrderIndex(Long courseId);
    List<Module> findByCourseId(Long courseId);
}