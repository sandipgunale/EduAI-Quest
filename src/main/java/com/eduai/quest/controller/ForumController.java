package com.eduai.quest.controller;

import com.eduai.quest.model.dto.forum.ForumPostRequest;
import com.eduai.quest.model.dto.forum.ForumPostResponse;
import com.eduai.quest.model.dto.forum.ForumCommentRequest;
import com.eduai.quest.model.dto.forum.ForumCommentResponse;
import com.eduai.quest.service.ForumService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@RequiredArgsConstructor
@Tag(name = "Forum", description = "Discussion forum APIs")
public class ForumController {

    private final ForumService forumService;

    @PostMapping("/posts")
    @Operation(summary = "Create a new forum post")
    public ResponseEntity<ForumPostResponse> createPost(@Valid @RequestBody ForumPostRequest request) {
        ForumPostResponse response = forumService.createPost(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/course/{courseId}")
    @Operation(summary = "Get forum posts for a course")
    public ResponseEntity<Page<ForumPostResponse>> getCoursePosts(
            @PathVariable Long courseId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ForumPostResponse> posts = forumService.getPostsByCourse(courseId, pageable);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/posts/{postId}")
    @Operation(summary = "Get forum post by ID")
    public ResponseEntity<ForumPostResponse> getPost(@PathVariable Long postId) {
        ForumPostResponse post = forumService.getPostById(postId);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/posts/{postId}/comments")
    @Operation(summary = "Add comment to a post")
    public ResponseEntity<ForumCommentResponse> addComment(
            @PathVariable Long postId,
            @Valid @RequestBody ForumCommentRequest request) {
        ForumCommentResponse response = forumService.addComment(postId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/{postId}/comments")
    @Operation(summary = "Get comments for a post")
    public ResponseEntity<List<ForumCommentResponse>> getPostComments(@PathVariable Long postId) {
        List<ForumCommentResponse> comments = forumService.getCommentsByPost(postId);
        return ResponseEntity.ok(comments);
    }

    @PutMapping("/posts/{postId}/pin")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Pin or unpin a post")
    public ResponseEntity<ForumPostResponse> togglePinPost(@PathVariable Long postId) {
        ForumPostResponse response = forumService.togglePinPost(postId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/posts/search")
    @Operation(summary = "Search forum posts")
    public ResponseEntity<Page<ForumPostResponse>> searchPosts(
            @RequestParam Long courseId,
            @RequestParam String query,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<ForumPostResponse> posts = forumService.searchPosts(courseId, query, pageable);
        return ResponseEntity.ok(posts);
    }
}