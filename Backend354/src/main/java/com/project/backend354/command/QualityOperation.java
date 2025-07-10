package com.project.backend354.command;

import lombok.Getter;

@Getter
public class QualityOperation extends ImageOperation {
    private final int quality; // 1-100

    public QualityOperation(String inputPath, String outputPath, int quality) {
        super(inputPath, outputPath);
        this.quality = quality;
    }

    @Override
    public String buildCommandPart() {
        return String.format("-quality %d", quality);
    }
}
