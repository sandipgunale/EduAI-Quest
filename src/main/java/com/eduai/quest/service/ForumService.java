package com.eduai.quest.service;

import com.eduai.quest.model.dto.forum.ForumPostRequest;
import com.eduai.quest.model.dto.forum.ForumPostResponse;
import com.eduai.quest.model.dto.forum.ForumCommentRequest;
import com.eduai.quest.model.dto.forum.ForumCommentResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ForumService {
    ForumPostResponse createPost(ForumPostRequest request);
    ForumPostResponse getPostById(Long postId);
    Page<ForumPostResponse> getPostsByCourse(Long courseId, Pageable pageable);
    ForumCommentResponse addComment(Long postId, ForumCommentRequest request);
    List<ForumCommentResponse> getCommentsByPost(Long postId);
    ForumPostResponse togglePinPost(Long postId);
    Page<ForumPostResponse> searchPosts(Long courseId, String query, Pageable pageable);
}