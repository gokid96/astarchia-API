package com.astarchia.domain.post.controller;


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

    //생성 // Long userId 받는거  시큐리티 설정 후 @AuthenticationPrincipal 로 받기
    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(@RequestParam Long userId, @RequestBody PostCreateRequestDTO request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.createPost(userId, request));
    }

    // 내글 목록
    @GetMapping("/my")
    public ResponseEntity<Page<PostResponseDTO>> getMyPosts(@RequestParam Long userId, Pageable pageable) {
        return ResponseEntity.ok(postService.getMyPosts(userId, pageable));
    }

    //공개 글 목록
    @GetMapping("/public")
    public ResponseEntity<Page<PostResponseDTO>> getPublicPosts(@RequestParam Long userId, Pageable pageable) {
        return ResponseEntity.ok(postService.getPublicPosts(userId, pageable));
    }

    //ID로 단건 조회 (관리용)
    @GetMapping("/id/{postId}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPostById(postId));
    }

    //slug로 단건 조회 (공개용, SEO)
    @GetMapping("/{slug}")
    public ResponseEntity<PostResponseDTO> getPostBySlug(@PathVariable String slug) {
        return ResponseEntity.ok(postService.getPostBySlug(slug));
    }

    //수정
    @PatchMapping("/{postId}")
    public ResponseEntity<PostResponseDTO> updatePost(@RequestParam Long userId, @PathVariable Long postId, @RequestBody PostCreateRequestDTO request) {
        return ResponseEntity.ok(postService.updatePost(userId, postId, request));
    }

    //삭제
    @DeleteMapping("/{postId}")
    public ResponseEntity<Void> deletePost(@RequestParam Long userId, @PathVariable Long postId) {
        postService.deletePost(userId, postId);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{postId}/move")
    public ResponseEntity<PostResponseDTO> moveToFolder(
            @PathVariable Long postId,
            @RequestParam Long userId,
            @RequestBody MovePostRequestDTO request) {
        Post updatedPost = postService.moveToFolder(userId, postId, request.getFolderId());
        return ResponseEntity.ok(PostResponseDTO.from(updatedPost));
    }

}
