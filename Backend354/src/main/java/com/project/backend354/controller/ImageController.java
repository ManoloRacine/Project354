package com.project.backend354.controller;

import com.project.backend354.dto.ImageUploadResponse;
import com.project.backend354.service.FileDownloadService;
import com.project.backend354.service.ImageProcessingService;
import com.project.backend354.service.ImageStorageService;
import com.project.backend354.utils.validation.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@Slf4j
public class ImageController {

    private final ImageStorageService storageService;
    private final FileValidator fileValidator;
    private final ImageProcessingService processingService;
    private final FileDownloadService downloadService;

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

    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadImage(@PathVariable String filename) {
        Resource resource = downloadService.getFileResource(filename);

        if (resource == null) {
            return ResponseEntity.notFound().build();
        }

        String contentType = downloadService.getContentType(filename);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .body(resource);
    }

    @PostMapping("/apply")
    public ResponseEntity<ImageUploadResponse> applyOperations(
            @RequestParam String filename,
            @RequestBody List<Map<String, Object>> operations) {

        String outputFilename = processingService.processImage(filename, operations);

        return ResponseEntity.ok(ImageUploadResponse.builder()
                .message("Operations applied successfully")
                .filename(outputFilename)
                .path(outputFilename)
                .build());
    }
}