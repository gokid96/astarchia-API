package com.astarchia.domain.post.repository;

import com.astarchia.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findByAuthor_UserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Optional<Post> findByAuthor_UserIdAndPostId(Long userId, Long postId);

}