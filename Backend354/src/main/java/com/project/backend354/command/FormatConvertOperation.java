package com.project.backend354.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FormatConvertOperation extends ImageOperation {
    private final String format; // jpg, png, webp, etc.

    @Override
    public String buildCommandPart() {
        return String.format("-format %s", format);
    }
}
