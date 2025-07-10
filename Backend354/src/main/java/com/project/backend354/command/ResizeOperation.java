package com.project.backend354.command;

import lombok.Getter;

@Getter
public class ResizeOperation extends ImageOperation {
    private final int width;
    private final int height;

    public ResizeOperation(String inputPath, String outputPath, int width, int height) {
        super(inputPath, outputPath);
        this.width = width;
        this.height = height;
    }

    @Override
    public String buildCommandPart() {
        return String.format("-resize %dx%d", width, height);
    }
}