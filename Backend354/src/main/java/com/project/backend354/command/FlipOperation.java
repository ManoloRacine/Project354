package com.project.backend354.command;

import lombok.Getter;

@Getter
public class FlipOperation extends ImageOperation {
    private final boolean horizontal;
    private final boolean vertical;

    public FlipOperation(String inputPath, String outputPath, boolean horizontal, boolean vertical) {
        super(inputPath, outputPath);
        this.horizontal = horizontal;
        this.vertical = vertical;
    }

    @Override
    public String buildCommandPart() {
        StringBuilder command = new StringBuilder();
        if (horizontal) command.append("-flop ");
        if (vertical) command.append("-flip ");
        return command.toString().trim();
    }
}