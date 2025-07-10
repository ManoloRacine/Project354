package com.project.backend354.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class BrightnessContrastOperation extends ImageOperation {
    private final int brightness; // -100 to 100
    private final int contrast;   // -100 to 100

    @Override
    public String buildCommandPart() {
        return String.format("-brightness-contrast %d,%d", brightness, contrast);
    }
}
