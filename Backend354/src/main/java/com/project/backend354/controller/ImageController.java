package com.project.backend354.controller;

import com.project.backend354.service.ImageService;
import jakarta.servlet.http.HttpSession;
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

@RestController
@RequestMapping("/api/upload")
class ImageController {

    private static final Logger log = LoggerFactory.getLogger(ImageController.class);
    private final ImageService imageService;

    ImageController(ImageService imageUploadService) {
        this.imageService = imageUploadService;
    }

    @PostMapping(
            value = "/image",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<?> uploadImage(
            HttpSession session,
            @RequestParam("file") MultipartFile file) throws IOException {

        if (file.isEmpty()) {
            return ResponseEntity
                    .badRequest()
                    .body("File is empty");
        }

        if (!file.getContentType().startsWith("image/")) {
            return ResponseEntity
                    .badRequest()
                    .body("Only image files are allowed.");
        }

        Path sessionDir = (Path) session.getAttribute("uploadDir");

        if (sessionDir == null) {
            log.error("Session does not contain uploadDir attribute.");
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Session not initialized properly.");
        }

        String savedPath = imageService.upload(sessionDir, file);
        log.info("File uploaded to: {}", savedPath);
        return ResponseEntity.ok("Uploaded to: " + savedPath);
    }
}
