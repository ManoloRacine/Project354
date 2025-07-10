package com.project.backend354.service;

import com.project.backend354.command.ImageOperation;
import com.project.backend354.command.factory.ImageOperationFactory;
import com.project.backend354.config.SessionDirectoryProvider;
import com.project.backend354.exception.FileNotFoundException;
import com.project.backend354.exception.ImageProcessingException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ImageProcessingService {

    private final ImageOperationFactory operationFactory;
    private final SessionDirectoryProvider dirProvider;

    public String processImage(String filename, List<Map<String, Object>> operations) {
        validateOperations(operations);

        Path sessionDir = dirProvider.getDirectory();
        Path inputPath = sessionDir.resolve(filename);

        validateInputFile(inputPath, filename);

        String outputFilename = generateOutputFilename(filename, operations);
        Path outputPath = sessionDir.resolve(outputFilename);

        try {
            List<ImageOperation> imageOperations = createImageOperations(operations, inputPath, outputPath);
            executeOperations(imageOperations);

            log.info("Operations applied successfully to {}, output: {}", filename, outputFilename);
            return outputFilename;

        } catch (Exception e) {
            cleanupOutputFile(outputPath);
            throw new ImageProcessingException("Failed to apply operations", e);
        }
    }

    private void validateOperations(List<Map<String, Object>> operations) {
        if (operations == null || operations.isEmpty()) {
            throw new IllegalArgumentException("Operations list cannot be null or empty");
        }
    }

    private void validateInputFile(Path inputPath, String filename) {
        if (!Files.exists(inputPath)) {
            throw new FileNotFoundException("Input file not found: " + filename);
        }
    }

    private List<ImageOperation> createImageOperations(List<Map<String, Object>> operations, Path inputPath, Path outputPath) {
        return operations.stream()
                .map(opConfig -> operationFactory.createOperation(
                        (String) opConfig.get("type"),
                        inputPath.toString(),
                        outputPath.toString(),
                        opConfig))
                .toList();
    }

    private void executeOperations(List<ImageOperation> imageOperations) {
        if (!imageOperations.isEmpty()) {
            imageOperations.getFirst().execute(imageOperations);
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