package com.astarchia.domain.post.repository;

import com.astarchia.domain.category.entity.Category;
import com.astarchia.domain.category.entity.Visibility;
import com.astarchia.domain.post.entity.Post;
import com.astarchia.domain.post.entity.PostStatus;
import com.astarchia.domain.series.entity.Series;
import com.astarchia.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findBySlug(String slug);
    List<Post> findByAuthorOrderByCreatedAtDesc(Users author);
    List<Post> findByStatusAndVisibilityOrderByPublishedAtDesc(PostStatus status, Visibility visibility);
    List<Post> findByCategoryOrderByCreatedAtDesc(Category category);
    List<Post> findBySeriesOrderByCreatedAtAsc(Series series);
}