package com.project.backend354.command;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class QualityOperation extends ImageOperation {
    private final int quality; // 1-100

    @Override
    public String buildCommandPart() {
        return String.format("-quality %d", quality);
    }
}
