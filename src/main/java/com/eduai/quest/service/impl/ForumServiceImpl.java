package com.eduai.quest.service.impl;

import com.eduai.quest.model.dto.forum.ForumPostRequest;
import com.eduai.quest.model.dto.forum.ForumPostResponse;
import com.eduai.quest.model.dto.forum.ForumCommentRequest;
import com.eduai.quest.model.dto.forum.ForumCommentResponse;
import com.eduai.quest.model.entity.Course;
import com.eduai.quest.model.entity.ForumComment;
import com.eduai.quest.model.entity.ForumPost;
import com.eduai.quest.model.entity.User;
import com.eduai.quest.repository.CourseRepository;
import com.eduai.quest.repository.ForumCommentRepository;
import com.eduai.quest.repository.ForumPostRepository;
import com.eduai.quest.service.ForumService;
import com.eduai.quest.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ForumServiceImpl implements ForumService {

    private final ForumPostRepository forumPostRepository;
    private final ForumCommentRepository forumCommentRepository;
    private final CourseRepository courseRepository;
    private final UserService userService;

    @Override
    @Transactional
    public ForumPostResponse createPost(ForumPostRequest request) {
        User currentUser = userService.getCurrentUser();

        ForumPost post = new ForumPost();
        post.setTitle(request.getTitle());
        post.setContent(request.getContent());
        post.setAuthor(currentUser);
        post.setPinned(request.getPinned());

        if (request.getCourseId() != null) {
            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found"));
            post.setCourse(course);
        }

        ForumPost savedPost = forumPostRepository.save(post);
        return convertToPostResponse(savedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public ForumPostResponse getPostById(Long postId) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Forum post not found"));
        return convertToPostResponse(post);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ForumPostResponse> getPostsByCourse(Long courseId, Pageable pageable) {
        Page<ForumPost> posts = forumPostRepository.findByCourseId(courseId, pageable);
        return posts.map(this::convertToPostResponse);
    }

    @Override
    @Transactional
    public ForumCommentResponse addComment(Long postId, ForumCommentRequest request) {
        User currentUser = userService.getCurrentUser();
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Forum post not found"));

        ForumComment comment = new ForumComment();
        comment.setContent(request.getContent());
        comment.setAuthor(currentUser);
        comment.setPost(post);

        if (request.getParentCommentId() != null) {
            ForumComment parentComment = forumCommentRepository.findById(request.getParentCommentId())
                    .orElseThrow(() -> new RuntimeException("Parent comment not found"));
            comment.setParentComment(parentComment);
        }

        ForumComment savedComment = forumCommentRepository.save(comment);
        return convertToCommentResponse(savedComment);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ForumCommentResponse> getCommentsByPost(Long postId) {
        List<ForumComment> comments = forumCommentRepository.findByPostIdOrderByCreatedAtAsc(postId);
        return comments.stream()
                .map(this::convertToCommentResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ForumPostResponse togglePinPost(Long postId) {
        ForumPost post = forumPostRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Forum post not found"));

        post.setPinned(!post.isPinned());
        ForumPost updatedPost = forumPostRepository.save(post);

        return convertToPostResponse(updatedPost);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ForumPostResponse> searchPosts(Long courseId, String query, Pageable pageable) {
        Page<ForumPost> posts = forumPostRepository.searchInCourse(courseId, query, pageable);
        return posts.map(this::convertToPostResponse);
    }

    private ForumPostResponse convertToPostResponse(ForumPost post) {
        ForumPostResponse response = new ForumPostResponse();
        response.setId(post.getId());
        response.setTitle(post.getTitle());
        response.setContent(post.getContent());
        response.setAuthorName(post.getAuthor().getName());
        response.setAuthorEmail(post.getAuthor().getEmail());
        response.setPinned(post.isPinned());
        response.setClosed(post.isClosed());
        response.setCreatedAt(post.getCreatedAt());
        response.setUpdatedAt(post.getUpdatedAt());

        if (post.getCourse() != null) {
            response.setCourseId(post.getCourse().getId());
            response.setCourseTitle(post.getCourse().getTitle());
        }

        long commentCount = forumCommentRepository.countByPostId(post.getId());
        response.setCommentCount(commentCount);

        return response;
    }

    private ForumCommentResponse convertToCommentResponse(ForumComment comment) {
        ForumCommentResponse response = new ForumCommentResponse();
        response.setId(comment.getId());
        response.setContent(comment.getContent());
        response.setAuthorName(comment.getAuthor().getName());
        response.setAuthorEmail(comment.getAuthor().getEmail());
        response.setPostId(comment.getPost().getId());
        response.setCreatedAt(comment.getCreatedAt());
        response.setUpdatedAt(comment.getUpdatedAt());

        if (comment.getParentComment() != null) {
            response.setParentCommentId(comment.getParentComment().getId());
        }

        return response;
    }
}