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

@Component
@SessionScope
public class SessionUploadDirectory {
    private static final Logger log = LoggerFactory.getLogger(SessionUploadDirectory.class);
    private final Path uploadDir;

    public SessionUploadDirectory(@Value("${upload.base-dir}") String uploadRoot,
                                  HttpSession session) throws IOException {
        this.uploadDir = Paths.get(uploadRoot, session.getId());
        Files.createDirectories(uploadDir);
        log.info("Created session directory: {}", uploadDir);
    }

    public Path getDirectory() {
        return uploadDir;
    }

    @PreDestroy
    public void cleanUp() {
        try {
            FileSystemUtils.deleteRecursively(uploadDir);
            log.info("Deleted session directory: {}", uploadDir);
        } catch (IOException e) {
            log.error("Failed to delete session directory: {}", uploadDir, e);
        }
    }
}
