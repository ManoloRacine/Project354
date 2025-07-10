package com.project.backend354.command;

import lombok.Getter;

@Getter
public class BrightnessContrastOperation extends ImageOperation {
    private final int brightness; // -100 to 100
    private final int contrast;   // -100 to 100

    public BrightnessContrastOperation(String inputPath, String outputPath, int brightness, int contrast) {
        super(inputPath, outputPath);
        this.brightness = brightness;
        this.contrast = contrast;
    }

    @Override
    public String buildCommandPart() {
        return String.format("-brightness-contrast %d,%d", brightness, contrast);
    }
}
