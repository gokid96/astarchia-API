package com.astarchia.domain.folder.repository;

import com.astarchia.domain.folder.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    /*
    * 사용자 루트 폴더 조회
    * */
    List<Folder> findByUserIdAndParentIsNull( Long userId);
    /*
    * 특정 폴더의 하위 폴더들 조회
    * */
    List<Folder> findByParentId( Long parentId);


}
