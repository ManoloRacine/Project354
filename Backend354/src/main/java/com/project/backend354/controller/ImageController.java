package com.project.backend354.controller;

import com.project.backend354.config.SessionUploadDirectory;
import com.project.backend354.service.ImageStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;

@RestController
@RequestMapping("/api/images")
public class ImageController {
    private final ImageStorageService storageService;
    private final SessionUploadDirectory sessionDir;

    public ImageController(ImageStorageService storageService,
                           SessionUploadDirectory sessionDir) {
        this.storageService = storageService;
        this.sessionDir    = sessionDir;
    }

    @PostMapping(value = "/upload",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file)
            throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("File is empty.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            return ResponseEntity.badRequest()
                    .body("Only image files are allowed.");
        }

        Path saved = storageService.save(sessionDir.getDirectory(), file);
        return ResponseEntity.ok("Uploaded to: " + saved);
    }
}
