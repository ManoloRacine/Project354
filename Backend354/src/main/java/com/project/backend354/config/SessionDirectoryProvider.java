package com.project.backend354.config;

import com.project.backend354.exception.FileStorageException;
import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
public class SessionDirectoryProvider {

    private static final Logger log = LoggerFactory.getLogger(SessionDirectoryProvider.class);

    private final Path sessionDir;

    public SessionDirectoryProvider(
            @Value("${upload.base-dir}") String uploadRoot,
            HttpSession session) {

        if (!StringUtils.hasText(uploadRoot)) {
            throw new FileStorageException("Upload base directory not configured", null);
        }

        String uniqueFolder = UUID.randomUUID().toString();
        this.sessionDir = Paths.get(uploadRoot, session.getId(), uniqueFolder);

        try {
            Files.createDirectories(sessionDir);
            log.info("Session upload dir created: {}", sessionDir);
        } catch (IOException e) {
            log.error("Failed to create session upload directory: {}", sessionDir, e);
            throw new FileStorageException("Could not create upload directory: " + sessionDir, e);
        } catch (SecurityException e) {
            log.error("Permission denied creating directory: {}", sessionDir, e);
            throw new FileStorageException("Permission denied creating upload directory", e);
        }
    }

    public Path getDirectory() {
        if (!Files.exists(sessionDir)) {
            log.warn("Session directory no longer exists: {}", sessionDir);
            throw new FileStorageException("Session directory has been removed", null);
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
            log.error("Failed to delete session dir: {}", sessionDir, e);
        }
    }
}