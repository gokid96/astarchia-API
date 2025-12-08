package com.astarchia.domain.post.controller;

import com.astarchia.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import com.astarchia.domain.post.dto.request.MovePostRequestDTO;
import com.astarchia.domain.post.dto.request.PostCreateRequestDTO;
import com.astarchia.domain.post.dto.response.PostResponseDTO;
import com.astarchia.domain.post.entity.Post;
import com.astarchia.domain.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/posts")
public class PostController {
    private final PostService postService;

    //생성 // Long userDetails.getUserId() 받는거  시큐리티 설정 후 @AuthenticationPrincipal 로 받기
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody PostCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(userDetails.getUserId(), request));
    }

    // 내글 목록
    @GetMapping("/my")
    public ResponseEntity<Page<PostResponseDTO>> getMyPosts(@AuthenticationPrincipal CustomUserDetails userDetails, Pageable pageable) {
        return ResponseEntity.ok(postService.getMyPosts(userDetails.getUserId(), pageable));
    }

    //ID로 단건 조회 (관리용)
    @GetMapping("/id/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    //수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId, @RequestBody PostCreateRequestDTO request) {
        return ResponseEntity.ok(postService.updatePost(userDetails.getUserId(), postId, request));
    }

    //삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@AuthenticationPrincipal CustomUserDetails userDetails, @PathVariable Long postId) {
        postService.deletePost(userDetails.getUserId(), postId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{postId}/move")
    public ResponseEntity<PostResponseDTO> moveToFolder(
            @PathVariable Long postId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MovePostRequestDTO request) {
        Post updatedPost = postService.moveToFolder(userDetails.getUserId(), postId, request.getFolderId());
        return ResponseEntity.ok(PostResponseDTO.from(updatedPost));
    }

}
