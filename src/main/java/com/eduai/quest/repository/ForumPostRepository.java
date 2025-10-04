package com.eduai.quest.repository;

import com.eduai.quest.model.entity.ForumPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumPostRepository extends JpaRepository<ForumPost, Long> {
    Page<ForumPost> findByCourseId(Long courseId, Pageable pageable);
    Page<ForumPost> findByAuthorId(Long authorId, Pageable pageable);
    List<ForumPost> findByPinnedTrueAndCourseId(Long courseId);

    @Query("SELECT fp FROM ForumPost fp WHERE fp.course.id = :courseId AND " +
            "(LOWER(fp.title) LIKE LOWER(CONCAT('%', :query, '%')) OR " +
            "LOWER(fp.content) LIKE LOWER(CONCAT('%', :query, '%')))")
    Page<ForumPost> searchInCourse(@Param("courseId") Long courseId,
                                   @Param("query") String query,
                                   Pageable pageable);

    long countByCourseId(Long courseId);
}