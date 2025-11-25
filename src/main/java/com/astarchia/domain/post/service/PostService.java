package com.astarchia.domain.post.service;


import com.astarchia.domain.category.entity.Visibility;
import com.astarchia.domain.folder.entity.Folder;
import com.astarchia.domain.folder.repository.FolderRepository;
import com.astarchia.domain.post.dto.request.PostCreateRequestDTO;
import com.astarchia.domain.post.dto.response.PostResponseDTO;
import com.astarchia.domain.post.entity.Post;
import com.astarchia.domain.post.entity.PostStatus;
import com.astarchia.domain.post.repository.PostRepository;
import com.astarchia.domain.user.entity.Users;
import com.astarchia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final FolderRepository folderRepository;

    /*
     * 생성
     */
    @Transactional
    public PostResponseDTO createPost(Long userId, PostCreateRequestDTO request) {
        Users author = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 폴더 조회 추가
        Folder folder = null;
        if (request.getFolderId() != null) {
            folder = folderRepository.findById(request.getFolderId())
                    .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));
        }

        Post post = Post.builder()
                .author(author)
                .title(request.getTitle())
                .content(request.getContent())
                .summary(request.getSummary())
                .thumbnailUrl(request.getThumbnailUrl())
                .slug(request.getSlug())
                .folder(folder) // ← 추가
                .build();

        Post savedPost = postRepository.save(post);
        return PostResponseDTO.from(savedPost);
    }

    /**
     * 게시글을 다른 폴더로 이동
     */
    @Transactional
    public Post moveToFolder(Long userId, Long postId, Long folderId) {
        log.info("=== moveToFolder 시작 ===");
        log.info("userId: {}, postId: {}, folderId: {}", userId, postId, folderId);

        Post post = postRepository.findByAuthor_UserIdAndPostId(userId, postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        log.info("Post 조회 완료: {}", post.getPostId());

        if (folderId != null) {
            Folder folder = folderRepository.findById(folderId)
                    .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

            if (!folder.getUser().getUserId().equals(userId)) {
                throw new IllegalArgumentException("해당 폴더에 접근할 수 없습니다.");
            }

            post.updateFolder(folder);
            log.info("폴더로 이동 완료: {}", folderId);
        } else {
            post.updateFolder(null);
            log.info("루트로 이동 완료");
        }

        log.info("=== moveToFolder 종료 ===");

        return post;
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
        log.info("getContent: {},", request.getContent());
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
