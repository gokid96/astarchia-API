package com.astarchia.domain.post.repository;

import com.astarchia.domain.category.entity.Category;
import com.astarchia.domain.category.entity.Visibility;
import com.astarchia.domain.post.entity.Post;
import com.astarchia.domain.post.entity.PostStatus;
import com.astarchia.domain.series.entity.Series;
import com.astarchia.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>{
    Optional<Post> findBySlug(String slug);
    boolean existsBySlug(String slug);

    Page<Post> findByAuthor_UserIdOrderByCreatedAtDesc(Long userId, Pageable pageable);
    Page<Post> findByAuthor_UserIdAndStatusAndVisibilityOrderByPublishedAtDesc(Long userId, PostStatus status, Visibility visibility , Pageable pageable);
    Optional<Post> findByAuthor_UserIdAndPostId(Long userId, Long postId);
    List<Post> findByCategoryOrderByCreatedAtDesc(Category category);
    List<Post> findBySeriesOrderByCreatedAtAsc(Series series);
}