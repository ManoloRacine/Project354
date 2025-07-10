package com.project.backend354.service;

import com.project.backend354.config.SessionDirectoryProvider;
import com.project.backend354.exception.FileNotFoundException;
import com.project.backend354.exception.FileStorageException;
import lombok.Getter;
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
    private String lastUploadedFile;

    public Path save(MultipartFile file) {
        Path targetDir = dirProvider.getDirectory();
        String originalFilename = file.getOriginalFilename();
        Path target = targetDir.resolve(originalFilename);

        try (InputStream inputStream = file.getInputStream()) {
            Files.copy(inputStream, target, StandardCopyOption.REPLACE_EXISTING);
            lastUploadedFile = originalFilename;
            log.info("Successfully saved image to {}", target);
        } catch (IOException e) {
            throw new FileStorageException("Failed to save image file", e);
        }

        return target;
    }

    public String getLastUploadedFile() {
        if (lastUploadedFile == null) {
            throw new FileNotFoundException("No file has been uploaded yet");
        }
        return lastUploadedFile;
    }
}