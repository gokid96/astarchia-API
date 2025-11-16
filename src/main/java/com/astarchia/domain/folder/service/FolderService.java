package com.astarchia.domain.folder.service;

import com.astarchia.domain.folder.dto.request.FolderCreateRequestDTO;
import com.astarchia.domain.folder.dto.request.FolderUpdateRequestDTO;
import com.astarchia.domain.folder.dto.response.FolderResponseDTO;
import com.astarchia.domain.folder.entity.Folder;
import com.astarchia.domain.folder.repository.FolderRepository;
import com.astarchia.domain.user.entity.Users;
import com.astarchia.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    /**
     * 폴더 생성
     */
    @Transactional
    public FolderResponseDTO createFolder(Long userId, FolderCreateRequestDTO request) {
        // 사용자 조회
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // 부모 폴더 조회 (있으면)
        Folder parent = null;
        if (request.getParentId() != null) {
            parent = folderRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("부모 폴더를 찾을 수 없습니다."));

            // 권한 체크 (부모 폴더가 내 폴더인지)
            if (!parent.getUser().getUserId().equals(userId)) {
                throw new IllegalArgumentException("해당 폴더에 접근 권한이 없습니다.");
            }
        }

        // 폴더 생성
        Folder folder = Folder.builder()
                .user(user)
                .parent(parent)
                .name(request.getName())
                .orderIndex(request.getOrderIndex())
                .build();

        Folder savedFolder = folderRepository.save(folder);
        return FolderResponseDTO.from(savedFolder);
    }

    /**
     * 폴더 수정
     */
    @Transactional
    public FolderResponseDTO updateFolder(Long userId, Long folderId, FolderUpdateRequestDTO request) {
        // 폴더 조회 및 권한 체크
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        if (!folder.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 폴더를 수정할 권한이 없습니다.");
        }

        // 폴더명 수정
        folder.updateName(request.getName());

        return FolderResponseDTO.from(folder);
    }

    /**
     * 폴더 삭제
     */
    @Transactional
    public void deleteFolder(Long userId, Long folderId) {
        // 폴더 조회 및 권한 체크
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        if (!folder.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 폴더를 삭제할 권한이 없습니다.");
        }

        // 삭제 (cascade로 하위 폴더들도 자동 삭제)
        folderRepository.delete(folder);
    }

    /**
     * 루트 폴더 목록 조회
     */
    public List<FolderResponseDTO> getRootFolders(Long userId) {
        List<Folder> folders = folderRepository.findByUserUserIdAndParentIsNull(userId);
        return folders.stream()
                .map(FolderResponseDTO::from)
                .toList();
    }

    /**
     * 하위 폴더 목록 조회
     */
    public List<FolderResponseDTO> getChildFolders(Long userId, Long parentId) {
        // 부모 폴더 조회 및 권한 체크
        Folder parent = folderRepository.findById(parentId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        if (!parent.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("해당 폴더에 접근 권한이 없습니다.");
        }

        // 하위 폴더 조회
        List<Folder> children = folderRepository.findByParentFolderId(parentId);
        return children.stream()
                .map(FolderResponseDTO::from)
                .toList();
    }
}