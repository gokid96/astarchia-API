package com.astarchia.domain.tag.repository;

import com.astarchia.domain.post.entity.Post;
import com.astarchia.domain.tag.entity.PostTag;
import com.astarchia.domain.tag.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostTagRepository extends JpaRepository<PostTag, Long> {
    List<PostTag> findByPost(Post post);
    List<PostTag> findByTag(Tag tag);
    boolean existsByPostAndTag(Post post, Tag tag);
    void deleteByPost(Post post);
    void deleteByPostAndTag(Post post, Tag tag);
}