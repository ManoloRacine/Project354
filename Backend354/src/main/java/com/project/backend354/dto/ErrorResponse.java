package com.project.backend354.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.project.backend354.utils.AppConstants;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class ErrorResponse {

    private String error;
    private String message;

    @JsonFormat(pattern = AppConstants.TIMESTAMP_PATTERN)
    @Builder.Default
    private LocalDateTime timestamp = LocalDateTime.now();
}
