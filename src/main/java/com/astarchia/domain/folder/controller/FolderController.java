package com.astarchia.domain.folder.controller;


import com.astarchia.domain.folder.dto.request.FolderCreateRequestDTO;
import com.astarchia.domain.folder.dto.request.FolderUpdateRequestDTO;
import com.astarchia.domain.folder.dto.response.FolderResponseDTO;
import com.astarchia.domain.folder.service.FolderService;
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
    public ResponseEntity<FolderResponseDTO> createFolder(@RequestParam Long userId, @RequestBody FolderCreateRequestDTO request) {
        FolderResponseDTO folder = folderService.createFolder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(folder);
    }

    @PatchMapping("/{folderId}")
    public ResponseEntity<FolderResponseDTO> updateFolder(
            @PathVariable Long folderId,
            @RequestParam Long userId,
            @RequestBody FolderUpdateRequestDTO request) {
        FolderResponseDTO folder = folderService.updateFolder(userId, folderId, request);
        return ResponseEntity.ok(folder);
    }

    @DeleteMapping("/{folderId}")
    public ResponseEntity<Void> deleteFolder(
            @PathVariable Long folderId,
            @RequestParam Long userId) {
        folderService.deleteFolder(userId, folderId);
        return ResponseEntity.noContent().build();
    }


    @GetMapping
    public ResponseEntity<List<FolderResponseDTO>> getRootFolders(
            @RequestParam Long userId) {
        List<FolderResponseDTO> folders = folderService.getRootFolders(userId);
        return ResponseEntity.ok(folders);
    }


    @GetMapping("/{folderId}/children")
    public ResponseEntity<List<FolderResponseDTO>> getChildFolders(
            @PathVariable Long folderId,
            @RequestParam Long userId) {
        List<FolderResponseDTO> folders = folderService.getChildFolders(userId, folderId);
        return ResponseEntity.ok(folders);
    }
}