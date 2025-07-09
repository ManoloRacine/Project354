package com.project.backend354.controller;

import com.project.backend354.dto.ImageUploadResponse;
import com.project.backend354.service.ImageStorageService;
import com.project.backend354.utils.validation.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageStorageService storageService;
    private final FileValidator fileValidator;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ImageUploadResponse> uploadImage(@RequestParam MultipartFile file) {
        fileValidator.validateImage(file);
        Path savedPath = storageService.save(file);

        log.info("Image uploaded successfully: {}", savedPath.getFileName());

        ImageUploadResponse response = ImageUploadResponse.builder()
                .message("Upload successful")
                .filename(savedPath.getFileName().toString())
                .path(savedPath.toString())
                .build();

        return ResponseEntity.ok(response);
    }
}
