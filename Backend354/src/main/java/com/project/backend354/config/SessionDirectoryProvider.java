package com.project.backend354.config;

import com.project.backend354.exception.FileStorageException;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.context.annotation.SessionScope;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@SessionScope
@Slf4j
public class SessionDirectoryProvider {

    private final Path sessionDir;

    public SessionDirectoryProvider(@Value("${upload.base-dir}") String uploadRoot, HttpSession session) {

        if (!StringUtils.hasText(uploadRoot)) {
            throw new FileStorageException("Upload base directory not configured");
        }

        String uniqueFolder = UUID.randomUUID().toString();
        this.sessionDir = Paths.get(uploadRoot, session.getId(), uniqueFolder);

        try {
            Files.createDirectories(sessionDir);
            log.info("Session upload dir created: {}", sessionDir);
        } catch (IOException e) {
            throw new FileStorageException("Could not create upload directory: " + sessionDir, e);
        } catch (SecurityException e) {
            throw new FileStorageException("Permission denied creating upload directory", e);
        }
    }

    public Path getDirectory() {
        if (!Files.exists(sessionDir)) {
            throw new FileStorageException("Session directory has been removed");
        }
        return sessionDir;
    }

    @PreDestroy
    public void cleanUp() {
        if (!Files.exists(sessionDir)) {
            log.debug("Session directory already cleaned up: {}", sessionDir);
            return;
        }

        try {
            FileSystemUtils.deleteRecursively(sessionDir);
            log.info("Session upload dir deleted: {}", sessionDir);
        } catch (IOException e) {
            throw new FileStorageException("Failed to delete session dir: " + sessionDir, e);
        }
    }
}
