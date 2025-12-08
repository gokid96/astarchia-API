package com.astarchia.domain.folder.controller;


import com.astarchia.domain.folder.dto.request.FolderCreateRequestDTO;
import com.astarchia.domain.folder.dto.request.FolderUpdateRequestDTO;
import com.astarchia.domain.folder.dto.request.MoveFolderRequestDTO;
import com.astarchia.domain.folder.dto.response.FolderResponseDTO;
import com.astarchia.domain.folder.entity.Folder;
import com.astarchia.domain.folder.service.FolderService;
import com.astarchia.global.security.CustomUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/folders")
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<FolderResponseDTO> createFolder(@AuthenticationPrincipal CustomUserDetails userDetails, @RequestBody FolderCreateRequestDTO request) {
        FolderResponseDTO folder = folderService.createFolder(userDetails.getUserId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(folder);
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderResponseDTO> updateFolder(
            @PathVariable Long folderId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody FolderUpdateRequestDTO request) {
        FolderResponseDTO folder = folderService.updateFolder(userDetails.getUserId(), folderId, request);
        return ResponseEntity.ok(folder);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @PathVariable Long folderId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        folderService.deleteFolder(userDetails.getUserId(), folderId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<FolderResponseDTO>> getRootFolders(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<FolderResponseDTO> folders = folderService.getRootFolders(userDetails.getUserId());
        return ResponseEntity.ok(folders);
    }


    @GetMapping("/{folderId}/children")
    public ResponseEntity<List<FolderResponseDTO>> getChildFolders(
            @PathVariable Long folderId,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        List<FolderResponseDTO> folders = folderService.getChildFolders(userDetails.getUserId(), folderId);
        return ResponseEntity.ok(folders);
    }

    // /api/v1/folders
    @PatchMapping("/{folderId}/move")
    public ResponseEntity<FolderResponseDTO> moveToFolder(
            @PathVariable Long folderId,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody MoveFolderRequestDTO request) {
        Folder movedFolder = folderService.moveToFolder(userDetails.getUserId(), folderId, request.getParentId());
        return ResponseEntity.ok(FolderResponseDTO.from(movedFolder));
    }




}