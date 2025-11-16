package com.astarchia.domain.folder.repository;

import com.astarchia.domain.folder.entity.Folder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FolderRepository extends JpaRepository<Folder, Long> {
    /*
     * 사용자의 루트 폴더들 조회 (parent가 null)
     */
    List<Folder> findByUserUserIdAndParentIsNull(Long userId);

    /*
     * 특정 부모 폴더의 하위 폴더들 조회
     */
    List<Folder> findByParentFolderId(Long parentId);
    //              parent.folderId ↑

    /*
     * 사용자의 특정 폴더 조회 (권한 체크용)
     */
    Optional<Folder> findByFolderIdAndUserUserId(Long folderId, Long userId);

    /*
     * 사용자의 모든 폴더 조회
     */
    List<Folder> findByUserUserId(Long userId);

}
