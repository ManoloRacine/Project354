package com.project.backend354.service;

import com.project.backend354.config.SessionDirectoryProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
@Slf4j
public class FileDownloadService {

    private final SessionDirectoryProvider dirProvider;

    public Resource getFileResource(String filename) {
        Path sessionDir = dirProvider.getDirectory();
        Path filePath = sessionDir.resolve(filename);

        if (!Files.exists(filePath)) {
            return null;
        }

        return new FileSystemResource(filePath.toFile());
    }

    public String getContentType(String filename) {
        Path sessionDir = dirProvider.getDirectory();
        Path filePath = sessionDir.resolve(filename);

        try {
            String contentType = Files.probeContentType(filePath);
            return contentType != null ? contentType : MediaType.APPLICATION_OCTET_STREAM_VALUE;
        } catch (IOException e) {
            log.warn("Failed to determine content type for file: {}", filename, e);
            return MediaType.APPLICATION_OCTET_STREAM_VALUE;
        }
    }
}
