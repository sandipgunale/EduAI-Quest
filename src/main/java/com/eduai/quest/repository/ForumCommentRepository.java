package com.eduai.quest.repository;

import com.eduai.quest.model.entity.ForumComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ForumCommentRepository extends JpaRepository<ForumComment, Long> {
    List<ForumComment> findByPostIdOrderByCreatedAtAsc(Long postId);
    List<ForumComment> findByParentCommentId(Long parentCommentId);
    long countByPostId(Long postId);
    List<ForumComment> findByAuthorId(Long authorId);
}