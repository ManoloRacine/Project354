package com.project.backend354.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class CropOperation extends ImageOperation {
    private final int width;
    private final int height;
    private final int x;
    private final int y;

    @Override
    public String buildCommandPart() {
        return String.format("-crop %dx%d+%d+%d", width, height, x, y);
    }
}
