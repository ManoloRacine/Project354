package com.project.backend354.utils;

import com.project.backend354.exception.FileValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@Component
public class FileValidator {

    private static final Set<String> ALLOWED_TYPES
            = Set.of("image/jpeg", "image/jpg", "image/png", "image/gif", "image/webp");

    @Value("${upload.max-file-size:5242880}") // 5MB default
    private long maxFileSize;

    public void validateImage(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileValidationException("File cannot be empty");
        }

        if (file.getSize() > maxFileSize) {
            throw new FileValidationException("File size exceeds maximum allowed size");
        }

        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new FileValidationException("Only JPEG, PNG, GIF, and WebP images are allowed");
        }

        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new FileValidationException("Filename cannot be empty");
        }
    }
}
