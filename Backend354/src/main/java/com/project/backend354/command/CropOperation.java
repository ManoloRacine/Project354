package com.project.backend354.command;

import lombok.Getter;

@Getter
public class CropOperation extends ImageOperation {
    private final int width;
    private final int height;
    private final int x;
    private final int y;

    public CropOperation(String inputPath, String outputPath, int width, int height, int x, int y) {
        super(inputPath, outputPath);
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    @Override
    public String buildCommandPart() {
        return String.format("-crop %dx%d+%d+%d", width, height, x, y);
    }
}
