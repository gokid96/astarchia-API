package com.astarchia.domain.post.dto.request;


import com.astarchia.domain.category.entity.Visibility;
import com.astarchia.domain.post.entity.PostStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDTO {

    private String title;
    private String content;
    private String summary;
    private String thumbnailUrl;
    private String slug;
    private Long folderId;

    @Builder.Default
    private PostStatus status = PostStatus.DRAFT; //기본 임시저장

    @Builder.Default
    private Visibility visibility = Visibility.PUBLIC; //기본 공개

    private String categoryId; //카테고리
    private String seriesId;   //시리즈


}
