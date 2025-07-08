package com.project.backend354.controller;

import com.project.backend354.service.ImageStorageService;
import com.project.backend354.utils.FileValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
    private final ImageStorageService storageService;
    private final FileValidator fileValidator;

    public ImageController(ImageStorageService storageService, FileValidator fileValidator) {
        this.storageService = storageService;
        this.fileValidator = fileValidator;
    }

    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "File is empty"));
        }

        if (!fileValidator.isValidImage(file)) {
            return ResponseEntity
                    .badRequest()
                    .body(Map.of("error", "Only image files are allowed."));
        }

        try {
            Path saved = storageService.save(file);
            return ResponseEntity.ok(Map.of("message", "Upload successful", "path", saved.toString()));
        } catch (IOException e) {
            log.error("Upload failed", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save image. Please try again later."));
        }
    }
}
