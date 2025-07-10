package com.project.backend354.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class ResizeOperation extends ImageOperation {
    private final int width;
    private final int height;

    @Override
    public String buildCommandPart() {
        return String.format("-resize %dx%d", width, height);
    }
}