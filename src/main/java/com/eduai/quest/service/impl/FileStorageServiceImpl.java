package com.eduai.quest.service.impl;

import com.eduai.quest.service.FileStorageService;
import com.eduai.quest.util.FileStorageUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final FileStorageUtil fileStorageUtil;

    @Override
    public String storeFile(MultipartFile file, String fileType) {
        if (file.isEmpty()) {
            throw new RuntimeException("Failed to store empty file.");
        }

        // Validate file type
        validateFileType(file, fileType);

        // Store file based on type
        String subDirectory = getSubDirectory(fileType);
        return fileStorageUtil.storeFile(file, subDirectory);
    }

    @Override
    public void deleteFile(String filePath) {
        fileStorageUtil.deleteFile(filePath);
    }

    @Override
    public byte[] loadFile(String filePath) {
        try {
            return Files.readAllBytes(fileStorageUtil.loadFile(filePath));
        } catch (IOException e) {
            log.error("Could not load file: {}", filePath, e);
            throw new RuntimeException("Could not load file: " + e.getMessage());
        }
    }

    private void validateFileType(MultipartFile file, String fileType) {
        String fileName = file.getOriginalFilename();
        if (fileName == null) {
            throw new RuntimeException("File name cannot be null");
        }

        String extension = getFileExtension(fileName).toLowerCase();

        switch (fileType.toUpperCase()) {
            case "IMAGE":
                if (!isImageExtension(extension)) {
                    throw new RuntimeException("Invalid image file type: " + extension);
                }
                break;
            case "DOCUMENT":
                if (!isDocumentExtension(extension)) {
                    throw new RuntimeException("Invalid document file type: " + extension);
                }
                break;
            case "VIDEO":
                if (!isVideoExtension(extension)) {
                    throw new RuntimeException("Invalid video file type: " + extension);
                }
                break;
            default:
                throw new RuntimeException("Unsupported file type: " + fileType);
        }
    }

    private String getSubDirectory(String fileType) {
        switch (fileType.toUpperCase()) {
            case "IMAGE":
                return "images";
            case "DOCUMENT":
                return "documents";
            case "VIDEO":
                return "videos";
            default:
                return "others";
        }
    }

    private String getFileExtension(String fileName) {
        int lastDotIndex = fileName.lastIndexOf(".");
        return lastDotIndex == -1 ? "" : fileName.substring(lastDotIndex + 1);
    }

    private boolean isImageExtension(String extension) {
        return extension.matches("(jpg|jpeg|png|gif|bmp|webp)");
    }

    private boolean isDocumentExtension(String extension) {
        return extension.matches("(pdf|doc|docx|txt|ppt|pptx|xls|xlsx)");
    }

    private boolean isVideoExtension(String extension) {
        return extension.matches("(mp4|avi|mov|wmv|flv|webm)");
    }
}