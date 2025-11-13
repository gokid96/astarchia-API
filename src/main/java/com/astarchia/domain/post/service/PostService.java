package com.astarchia.domain.post.service;


import com.astarchia.domain.category.entity.Visibility;
import com.astarchia.domain.post.dto.request.PostCreateRequestDTO;
import com.astarchia.domain.post.dto.response.PostResponseDTO;
import com.astarchia.domain.post.entity.Post;
import com.astarchia.domain.post.entity.PostStatus;
import com.astarchia.domain.post.repository.PostRepository;
import com.astarchia.domain.user.entity.Users;
import com.astarchia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    /*
     * 생성
     */
    @Transactional
    public PostResponseDTO createPost(Long userId, PostCreateRequestDTO request) {
        // 1. 작성자 찾기
        Users author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        // 2. slug 중복 체크 (선택)
        if (postRepository.existsBySlug(request.getSlug())) {
            throw new IllegalArgumentException("이미 사용 중인 URL입니다.");
        }

        // 2. Post 엔티티 생성
        Post post = Post.builder()
                .author(author)
                .title(request.getTitle())
                .content(request.getContent())
                .summary(request.getSummary())
                .thumbnailUrl(request.getThumbnailUrl())
                .slug(request.getSlug())
                .build();
        // 3. 저장
        Post savedPost = postRepository.save(post);
        // 4. DTO 변환
        return PostResponseDTO.from(savedPost);
    }


    /*
     * 발행 (임시저장 → 발행)
     */

    /*
     * 조회수 증가
     */


    /*
     * 조회 - 내 글 목록 (작성자용)
     */
    public Page<PostResponseDTO> getMyPosts(Long userId ,Pageable pageable) {
        //내가 쓴글 전체 조회
        Page<Post> posts = postRepository.findByAuthor_UserIdOrderByCreatedAtDesc(userId,pageable);

        return posts.map(PostResponseDTO::from);

    }

    /*
     * 조회 - 공개된 글 목록 (방문자용)
     */
    public Page<PostResponseDTO> getPublicPosts(Long userId, Pageable pageable) {
        //공개된 글 조회 // (PostStatus status, Visibility visibility)
        Page<Post> posts = postRepository.findByAuthor_UserIdAndStatusAndVisibilityOrderByPublishedAtDesc
                (userId, PostStatus.PUBLISHED, Visibility.PUBLIC,pageable);
        return posts.map(PostResponseDTO::from);
    }

    /*
     * 조회 - 단건 (slug로)
     */
    public PostResponseDTO getPostBySlug(String slug) {
        Post post = postRepository.findBySlug(slug)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        //공개여부 확인
        if (post.getVisibility() != Visibility.PUBLIC ||
                post.getStatus() != PostStatus.PUBLISHED) {
            throw new IllegalArgumentException("게시글을 찾을 수 없습니다.");
        }

        return PostResponseDTO.from(post);
    }
    /*
     * 조회 - 단건 (ID로)
     */
    public PostResponseDTO getPostById(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        return PostResponseDTO.from(post);
    }
    /*
     * 수정 (내 글만)
     */
    @Transactional
    public PostResponseDTO updatePost(Long userId, Long postId, PostCreateRequestDTO request) {
        Post post = postRepository.findByAuthor_UserIdAndPostId(userId, postId)
                .orElseThrow(()->new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        //엔티티 값 변경
        post.updateContent(request.getContent());
        post.updateSummary(request.getSummary());
        post.updateThumbnailUrl(request.getThumbnailUrl());
        post.updateStatus(request.getStatus());
        post.updateVisibility(request.getVisibility());

        return PostResponseDTO.from(post);
        //트랜잭션 커밋 시점에 dirty checking 변경감지
    }
    /*
     * 삭제 (내 글만)
     */
    @Transactional
    public void deletePost(Long userId, Long postId) {
        Post post = postRepository.findByAuthor_UserIdAndPostId(userId,postId)
                .orElseThrow(()-> new IllegalArgumentException("게시글을 찾을 수 없습니다."));
        postRepository.delete(post);
    }


}
