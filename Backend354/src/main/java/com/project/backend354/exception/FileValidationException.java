package com.project.backend354.exception;

import org.springframework.http.HttpStatus;

public class FileValidationException extends BaseException {

    public FileValidationException(String message) {
        super(message, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }

    public FileValidationException(String message, Throwable cause) {
        super(message, cause, "VALIDATION_ERROR", HttpStatus.BAD_REQUEST);
    }
}
