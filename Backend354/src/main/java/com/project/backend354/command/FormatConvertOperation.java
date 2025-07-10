package com.project.backend354.command;

import lombok.Getter;

@Getter
public class FormatConvertOperation extends ImageOperation {
    private final String format; // jpg, png, webp, etc.

    public FormatConvertOperation(String inputPath, String outputPath, String format) {
        super(inputPath, outputPath);
        this.format = format;
    }

    @Override
    public String buildCommandPart() {
        return String.format("-format %s", format);
    }
}
