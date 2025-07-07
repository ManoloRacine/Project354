package com.project.backend354.service;

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
public class ImageService {
    private static final Logger log = LoggerFactory.getLogger(ImageService.class);

    public String upload(Path sessionDir, MultipartFile file) throws IOException {
        String fileName = Paths.get(file.getOriginalFilename()).getFileName().toString();
        Path target = sessionDir.resolve(fileName);
        Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);
        log.debug("Saved file to: {}", target);
        return target.toString();
    }
}
