package com.project.backend354.event;

import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.FileSystemUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Component
@WebListener
class ImageSessionListener implements HttpSessionListener {
    private static final Logger log = LoggerFactory.getLogger(ImageSessionListener.class);

    @Value("${file.upload-dir}")
    private String uploadRoot;

    @Override
    public void sessionCreated(HttpSessionEvent event) {
       String sessionId = event.getSession().getId();
       Path sessionDir = Paths.get(uploadRoot, sessionId);

       try {
           Files.createDirectories(sessionDir);
           event.getSession().setAttribute("uploadDir", sessionDir);
           log.info("Created session directory: {}", sessionDir);
       } catch (IOException e) {
           log.error("Failed to create session directory: {}", sessionDir, e);
       }
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent event) {
        String sessionId = event.getSession().getId();
        Path sessionDir = Paths.get(uploadRoot, sessionId);

        if (!Files.exists(sessionDir)) {
            log.warn("Session directory does not exist: {}", sessionDir);
            return;
        }

        try {
            FileSystemUtils.deleteRecursively(sessionDir);
            log.info("Deleted session directory: {}", sessionDir);
        } catch (IOException e) {
            log.error("Failed to delete session directory: {}", sessionDir, e);
        }
    }
}
