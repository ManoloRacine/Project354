package com.project.backend354.exception;

import com.project.backend354.dto.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(BaseException ex) {
        if (ex.getHttpStatus().is4xxClientError()) {
            log.warn("Client error: {}", ex.getMessage());
        } else {
            log.error("Server error", ex);
        }

        ErrorResponse error = ErrorResponse.builder()
                .error(ex.getErrorCode())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(ex.getHttpStatus()).body(error);
    }

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public ResponseEntity<ErrorResponse> handleMaxSizeException(MaxUploadSizeExceededException ex) {
        log.warn("File size exceeded: {}", ex.getMessage());

        ErrorResponse error = ErrorResponse.builder()
                .error("FILE_TOO_LARGE")
                .message("File size exceeds the maximum allowed limit")
                .build();

        return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        log.error("Unexpected error occurred", ex);

        ErrorResponse error = ErrorResponse.builder()
                .error("INTERNAL_ERROR")
                .message("An unexpected error occurred")
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}
