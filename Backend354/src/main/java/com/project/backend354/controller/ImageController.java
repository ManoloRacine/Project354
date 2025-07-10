package com.project.backend354.controller;

import com.project.backend354.command.ImageOperation;
import com.project.backend354.command.factory.ImageOperationFactory;
import com.project.backend354.config.SessionDirectoryProvider;
import com.project.backend354.dto.ImageUploadResponse;
import com.project.backend354.exception.FileNotFoundException;
import com.project.backend354.exception.ImageProcessingException;
import com.project.backend354.service.ImageStorageService;
import com.project.backend354.utils.validation.FileValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
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
    private final ImageOperationFactory operationFactory;
    private final SessionDirectoryProvider dirProvider;

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
        Path sessionDir = dirProvider.getDirectory();
        Path filePath = sessionDir.resolve(filename);

        if (!Files.exists(filePath)) {
            return ResponseEntity.notFound().build();
        }

        try {
            Resource resource = new FileSystemResource(filePath.toFile());

            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                    .body(resource);

        } catch (IOException e) {
            log.error("Error downloading file: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/apply")
    public ResponseEntity<ImageUploadResponse> applyOperations(
            @RequestParam String filename,
            @RequestBody List<Map<String, Object>> operations) {

        if (operations == null || operations.isEmpty()) {
            throw new IllegalArgumentException("Operations list cannot be null or empty");
        }

        Path sessionDir = dirProvider.getDirectory();
        Path inputPath = sessionDir.resolve(filename);

        if (!Files.exists(inputPath)) {
            throw new FileNotFoundException("Input file not found: " + filename);
        }

        String outputFilename = generateOutputFilename(filename, operations);
        Path outputPath = sessionDir.resolve(outputFilename);

        try {
            List<ImageOperation> imageOperations = operations.stream()
                    .map(opConfig -> operationFactory.createOperation(
                            (String) opConfig.get("type"),
                            inputPath.toString(),
                            outputPath.toString(),
                            opConfig))
                    .toList();

            if (!imageOperations.isEmpty()) {
                imageOperations.getFirst().execute(imageOperations);
            }

            log.info("Operations applied successfully to {}, output: {}", filename, outputFilename);

            return ResponseEntity.ok(ImageUploadResponse.builder()
                    .message("Operations applied successfully")
                    .filename(outputFilename)
                    .path(outputPath.toString())
                    .build());

        } catch (Exception e) {
            cleanupOutputFile(outputPath);
            throw new ImageProcessingException("Failed to apply operations", e);
        }
    }

    private void cleanupOutputFile(Path outputPath) {
        try {
            Files.deleteIfExists(outputPath);
        } catch (IOException cleanupEx) {
            log.warn("Failed to cleanup output file after error: {}", outputPath, cleanupEx);
        }
    }

    private String generateOutputFilename(String inputFilename, List<Map<String, Object>> operations) {
        String baseName = FilenameUtils.getBaseName(inputFilename);
        String extension = determineExtension(inputFilename, operations);
        return baseName + "_processed." + extension;
    }

    private String determineExtension(String inputFilename, List<Map<String, Object>> operations) {
        return operations.stream()
                .filter(op -> "format".equals(op.get("type")))
                .map(op -> (String) op.get("format"))
                .findFirst()
                .orElse(StringUtils.defaultIfBlank(FilenameUtils.getExtension(inputFilename), "png"));
    }
}