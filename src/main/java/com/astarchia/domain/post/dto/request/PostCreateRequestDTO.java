package com.astarchia.domain.post.dto.request;


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
    private Long folderId;
}
