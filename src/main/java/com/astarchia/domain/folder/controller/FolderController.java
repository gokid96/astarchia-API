package com.astarchia.domain.folder.controller;


import com.astarchia.domain.folder.dto.request.FolderCreateRequestDTO;
import com.astarchia.domain.folder.dto.request.FolderUpdateRequestDTO;
import com.astarchia.domain.folder.dto.response.FolderResponseDTO;
import com.astarchia.domain.folder.service.FolderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/folders")
public class FolderController {

    private final FolderService folderService;

    @PostMapping
    public ResponseEntity<FolderResponseDTO> createFolder(@RequestParam Long userId, @RequestBody FolderCreateRequestDTO folder) {
        return ResponseEntity.status(HttpStatus.CREATED).body(folderService.createFolder(userId, folder));
    }

    @PatchMapping
    public ResponseEntity<FolderResponseDTO> updateFolder(@RequestParam Long userId, @RequestParam Long folderId, @RequestBody FolderUpdateRequestDTO folder) {
        return ResponseEntity.ok(folderService.updateFolder(userId, folderId, folder));
    }

    @DeleteMapping
    public void deleteFolder(@RequestParam Long userId, @RequestParam Long folderId) {
        folderService.deleteFolder(userId, folderId);
    }

    @GetMapping
    public ResponseEntity<Iterable<FolderResponseDTO>> getFolderList(@RequestParam Long userId) {
        return ResponseEntity.ok(folderService.getFolderList(userId));
    }
}
