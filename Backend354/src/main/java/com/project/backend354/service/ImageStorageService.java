package com.project.backend354.service;

import com.project.backend354.config.SessionDirectoryProvider;
import com.project.backend354.exception.FileStorageException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageStorageService {

    private final SessionDirectoryProvider dirProvider;

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
