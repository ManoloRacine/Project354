package com.project.backend354;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@RestController
public class TestController {

    @GetMapping("/test")
    public String test() {
        System.out.println("test");
        return "testing backend";
    }

    @PostMapping("/resize")
    public ResponseEntity<byte[]> resizeImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam int width,
            @RequestParam int height
    ) throws IOException, InterruptedException {
        return processImageWithCommand(file, List.of(
                "-resize", width + "x" + height
        ), "resized");
    }

    @PostMapping("/rotate")
    public ResponseEntity<byte[]> rotateImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam double angle
    ) throws IOException, InterruptedException {
        return processImageWithCommand(file, List.of(
                "-rotate", String.valueOf(angle)
        ), "rotated");
    }

    @PostMapping("/crop")
    public ResponseEntity<byte[]> cropImage(
            @RequestParam("file") MultipartFile file,
            @RequestParam int width,
            @RequestParam int height,
            @RequestParam int x,
            @RequestParam int y
    ) throws IOException, InterruptedException {
        return processImageWithCommand(file, List.of(
                "-crop", width + "x" + height + "+" + x + "+" + y,
                "+repage"
        ), "cropped");
    }

    private ResponseEntity<byte[]> processImageWithCommand(MultipartFile file, List<String> magickOptions, String outputName)
            throws IOException, InterruptedException {

        Path inputFile = Files.createTempFile("input-", "-" + file.getOriginalFilename());
        Files.write(inputFile, file.getBytes());

        String extension = getFileExtension(file.getOriginalFilename());
        if (extension == null) {
            extension = "png";
        }
        Path outputFile = Files.createTempFile("output-", "." + extension);

        List<String> command = new java.util.ArrayList<>();
        command.add("magick");
        command.add(inputFile.toAbsolutePath().toString());
        command.addAll(magickOptions);
        command.add(outputFile.toAbsolutePath().toString());

        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectErrorStream(true);
        Process process = pb.start();

        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))
        ) {
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }

        int exitCode = process.waitFor();
        if (exitCode != 0) {
            throw new RuntimeException("ImageMagick operation failed with exit code " + exitCode);
        }

        byte[] processedImage = Files.readAllBytes(outputFile);
        Files.deleteIfExists(inputFile);
        Files.deleteIfExists(outputFile);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + outputName + "." + extension + "\"")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(processedImage);
    }

    private String getFileExtension(String filename) {
        if (filename == null) return null;
        int dotIndex = filename.lastIndexOf('.');
        return (dotIndex == -1) ? null : filename.substring(dotIndex + 1);
    }
}
