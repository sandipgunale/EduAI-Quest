package com.eduai.quest.controller;

import com.eduai.quest.service.FileStorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/files")
@RequiredArgsConstructor
@Tag(name = "Files", description = "File upload and download APIs")
public class FileController {

    private final FileStorageService fileStorageService;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Upload a file")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String fileType) {

        String filePath = fileStorageService.storeFile(file, fileType);

        Map<String, String> response = new HashMap<>();
        response.put("filePath", filePath);
        response.put("fileName", file.getOriginalFilename());
        response.put("message", "File uploaded successfully");

        return ResponseEntity.ok(response);
    }

    @GetMapping("/download")
    @Operation(summary = "Download a file")
    public ResponseEntity<byte[]> downloadFile(@RequestParam String filePath) {
        byte[] fileContent = fileStorageService.loadFile(filePath);

        String fileName = filePath.substring(filePath.lastIndexOf("/") + 1);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(fileContent);
    }

    @DeleteMapping
    @PreAuthorize("hasRole('TEACHER') or hasRole('ADMIN')")
    @Operation(summary = "Delete a file")
    public ResponseEntity<Map<String, String>> deleteFile(@RequestParam String filePath) {
        fileStorageService.deleteFile(filePath);

        Map<String, String> response = new HashMap<>();
        response.put("message", "File deleted successfully");

        return ResponseEntity.ok(response);
    }
}