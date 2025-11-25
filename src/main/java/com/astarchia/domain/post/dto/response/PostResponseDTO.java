package com.astarchia.domain.post.dto.response;


import com.astarchia.domain.category.entity.Visibility;
import com.astarchia.domain.post.entity.Post;
import com.astarchia.domain.post.entity.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDTO {

    private Long postId;
    private String title;
    private String content;
    private String summary;
    private String thumbnailUrl;
    private PostStatus status;
    private Visibility visibility;
    private Integer viewCount;
    private String slug;
    private LocalDateTime publishedAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long folderId;
    private String folderName;


    // 작성자 정보
    private Long authorId;
    private String authNickname;

    // 카테고리 정보
    private Long categoryId;
    private String categoryName;

    // 시리즈 정보
    private Long seriesId;
    private String seriesName;

    // Entity -> DTO
    public static PostResponseDTO from(Post post){
        return PostResponseDTO.builder()
                .postId(post.getPostId())
                .title(post.getTitle())
                .content(post.getContent())
                .summary(post.getSummary())
                .thumbnailUrl(post.getThumbnailUrl())
                .slug(post.getSlug())
                .status(post.getStatus())
                .visibility(post.getVisibility())
                .viewCount(post.getViewCount())
                .slug(post.getSlug())
                .publishedAt(post.getPublishedAt())
                .createdAt(post.getCreatedAt())
                .updatedAt(post.getUpdatedAt())
                .authorId(post.getAuthor().getUserId())
                .authNickname(post.getAuthor().getNickname())
                .folderId(post.getFolder() != null ? post.getFolder().getFolderId() : null) // ← 추가
                .folderName(post.getFolder() != null ? post.getFolder().getName() : null) // ← 선택
                .categoryId(post.getCategory() != null ? post.getCategory().getCategoryId() : null)
                .categoryName(post.getCategory() != null ? post.getCategory().getName() : null)
                .seriesId(post.getSeries() != null ? post.getSeries().getSeriesId() : null)
                .seriesName(post.getSeries() != null ? post.getSeries().getName() : null)

                .build();

    }


}
