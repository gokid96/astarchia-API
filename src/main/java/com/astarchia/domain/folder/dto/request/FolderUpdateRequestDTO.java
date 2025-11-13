package com.astarchia.domain.folder.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FolderUpdateRequestDTO {

    @Size(max = 50 ,message = "폴더이름은 50자를 넘을 수 없습니다.")
    private String name;

    private Long parentId;
}
