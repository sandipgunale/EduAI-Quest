package com.eduai.quest.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    String storeFile(MultipartFile file, String fileType);
    void deleteFile(String filePath);
    byte[] loadFile(String filePath);
}