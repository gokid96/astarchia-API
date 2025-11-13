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
import java.util.stream.Collectors;

/**
 * Service responsible for managing folder-related operations such as creation and update.
 * It interacts with the FolderRepository to perform CRUD operations on folder entities
 * and the UserRepository to validate user information.
 * The operations related to folder modification are transactional, ensuring data consistency.
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FolderService {

    private final FolderRepository folderRepository;
    private final UserRepository userRepository;

    @Transactional
    public FolderResponseDTO createFolder(Long userId, FolderCreateRequestDTO request) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));


        Folder parent = null;
        if(request.getParentId() != null){
            parent = folderRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("상위 폴더를 찾을 수 없습니다."));
        }

        Folder folder = Folder.builder()
                .user(user)
                .parent(parent)
                .name(request.getName())
                .build();

        Folder savedFolder = folderRepository.save(folder);
        return FolderResponseDTO.from(savedFolder);

    }

    @Transactional
    public FolderResponseDTO updateFolder(Long userId, Long folderId, FolderUpdateRequestDTO request) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

        //권한체크
        if (!folder.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("수정 권한이 없습니다");
        }
        //이름변경
        if (request.getName() != null && !request.getName().isEmpty()) {
            folder.updateName(request.getName());
        }
        //폴더이동
        if (request.getParentId() != null) {
            Folder newParent = folderRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다."));

            folder.moveToFolder(newParent);
        }

        //엔티티 값 변경
        return FolderResponseDTO.fromSimple(folder);
    }

    @Transactional
    public void deleteFolder(Long userId, Long folderId) {
        Folder folder = folderRepository.findById(folderId)
                .orElseThrow(() -> new IllegalArgumentException("폴더를 찾을 수 없습니다"));
        if (!folder.getUser().getUserId().equals(userId)) {
            throw new IllegalArgumentException("삭제 권한이 없습니다.");
        }
        folderRepository.delete(folder);

    }

    public List<FolderResponseDTO> getFolderList(Long userId) {
        // 루트 폴더 조회
        List<Folder> rootFolders = folderRepository.findByUserIdAndParentIsNull(userId);
        //변환
        return rootFolders.stream().map(FolderResponseDTO::from).collect(Collectors.toList());
    }


}
