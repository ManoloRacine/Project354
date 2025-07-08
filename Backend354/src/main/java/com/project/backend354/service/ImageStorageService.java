package com.project.backend354.service;

import com.project.backend354.config.SessionDirectoryProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
public class ImageStorageService {
    private static final Logger log = LoggerFactory.getLogger(ImageStorageService.class);

    private final SessionDirectoryProvider dirProvider;

    public ImageStorageService(SessionDirectoryProvider dirProvider) {
       this.dirProvider = dirProvider;
    }

    public Path save(MultipartFile file) throws IOException {
        Path targetDir = dirProvider.getDirectory();

        String fileName = Paths
                .get(file.getOriginalFilename())
                .getFileName()
                .toString();

        Path target = targetDir.resolve(fileName);

        Files.copy(file.getInputStream(),
                target,
                StandardCopyOption.REPLACE_EXISTING);

        log.info("Saved image to {}", target);
        return target;
    }
}
