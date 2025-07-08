package com.project.backend354.service;

import com.project.backend354.config.SessionDirectoryProvider;
import com.project.backend354.exception.FileStorageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
public class ImageStorageService {
    private static final Logger log = LoggerFactory.getLogger(ImageStorageService.class);

    private final SessionDirectoryProvider dirProvider;

    public ImageStorageService(SessionDirectoryProvider dirProvider) {
        this.dirProvider = dirProvider;
    }

    public Path save(MultipartFile file) {
        Path targetDir = dirProvider.getDirectory();
        String originalFilename = file.getOriginalFilename();
        Path target = targetDir.resolve(originalFilename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            log.info("Successfully saved image to {}", target);
            return target;
        } catch (IOException e) {
            log.error("Failed to save image to: {}", target, e);
            throw new FileStorageException("Failed to save image file", e);
        }
    }
}