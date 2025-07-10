package com.project.backend354.exception;

import org.springframework.http.HttpStatus;

public class ImageProcessingException extends BaseException {

    public ImageProcessingException(String message) {
        super(message, "IMAGE_PROCESSING_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public ImageProcessingException(String message, Throwable cause) {
        super(message, cause, "IMAGE_PROCESSING_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}