package com.project.backend354.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageUploadService {
    @Value("${file.upload-dir}")
    private String uploadDir;

    public String upload(String sessionId, MultipartFile file) throws IOException {
        Path sessionPath = Paths.get(uploadDir, sessionId);

        if (!Files.exists(sessionPath)) {
            Files.createDirectories(sessionPath);
        }

        String fileName = file.getOriginalFilename();
        assert fileName != null;
        Path target = sessionPath.resolve(fileName);

        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        return target.toString();
    }
}
