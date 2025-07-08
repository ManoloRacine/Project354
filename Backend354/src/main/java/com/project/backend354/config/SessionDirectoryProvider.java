package com.project.backend354.config;

import jakarta.annotation.PreDestroy;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;
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
        String uniqueFolder = UUID.randomUUID().toString();
        this.sessionDir = Paths.get(uploadRoot, session.getId(), uniqueFolder);

        try {
            Files.createDirectories(sessionDir);
            log.info("Session upload dir created: {}", sessionDir);
        } catch (IOException e) {
            log.error("Failed to create session upload directory: {}", sessionDir, e);
            throw new IllegalStateException("Could not create upload directory", e);
        }
    }

    public Path getDirectory() {
        return sessionDir;
    }

    @PreDestroy
    public void cleanUp() {
        try {
            FileSystemUtils.deleteRecursively(sessionDir);
            log.info("Session upload dir deleted: {}", sessionDir);
        } catch (IOException e) {
            log.error("Failed to delete session dir: {}", sessionDir, e);
        }
    }
}
