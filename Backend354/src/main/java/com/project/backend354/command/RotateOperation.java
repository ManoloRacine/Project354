package com.project.backend354.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class RotateOperation extends ImageOperation {
    private final double angle;

    @Override
    public String buildCommandPart() {
        return String.format("-rotate %.2f", angle);
    }
}
