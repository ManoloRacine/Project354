package com.project.backend354.command;

import com.project.backend354.exception.ImageProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.SuperBuilder;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Getter
@SuperBuilder
public abstract class ImageOperation {
    protected final String inputPath;
    protected final String outputPath;

    public abstract String buildCommandPart();

    public void execute(List<ImageOperation> operations) {
        List<String> commandList = buildCommandList(operations);
        executeImageMagickCommand(commandList);
    }

    private List<String> buildCommandList(List<ImageOperation> operations) {
        List<String> commandList = new ArrayList<>();
        commandList.add("magick");
        commandList.add(inputPath);

        operations.forEach(op -> {
            String commandPart = op.buildCommandPart();
            if (commandPart != null && !commandPart.trim().isEmpty()) {
                String[] parts = commandPart.split("\\s+");
                commandList.addAll(Arrays.asList(parts));
            }
        });

        commandList.add(outputPath);
        return commandList;
    }

    private void executeImageMagickCommand(List<String> commandList) {
        try {
            ProcessBuilder pb = new ProcessBuilder(commandList);
            pb.redirectErrorStream(true);
            Process process = pb.start();

            String output = captureProcessOutput(process);
            int exitCode = process.waitFor();

            handleProcessResult(exitCode, output);
        } catch (IOException e) {
            throw new ImageProcessingException("Failed to execute ImageMagick command", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ImageProcessingException("Image processing was interrupted", e);
        }
    }

    private String captureProcessOutput(Process process) throws IOException {
        StringBuilder output = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(process.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
        }
        return output.toString();
    }

    private void handleProcessResult(int exitCode, String output) {
        if (exitCode == 0) {
            log.info("ImageMagick command executed successfully");
        } else {
            log.error("ImageMagick error output: {}", output);
            throw new ImageProcessingException("Image processing failed with exit code: " + exitCode +
                    ". Error: " + output);
        }
    }
}
