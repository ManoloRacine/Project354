package com.project.backend354.command;

import lombok.Getter;

@Getter
public class RotateOperation extends ImageOperation {
    private final double angle;

    public RotateOperation(String inputPath, String outputPath, double angle) {
        super(inputPath, outputPath);
        this.angle = angle;
    }

    @Override
    public String buildCommandPart() {
        return String.format("-rotate %.2f", angle);
    }
}
