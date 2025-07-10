package com.project.backend354.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class FlipOperation extends ImageOperation {
    private final boolean horizontal;
    private final boolean vertical;

    @Override
    public String buildCommandPart() {
        StringBuilder command = new StringBuilder();
        if (horizontal) command.append("-flop ");
        if (vertical) command.append("-flip ");
        return command.toString().trim();
    }
}