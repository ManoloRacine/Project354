package com.project.backend354.exception;

import org.springframework.http.HttpStatus;

public class FileStorageException extends BaseException {

    public FileStorageException(String message) {
        super(message, "FILE_STORAGE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message, cause, "FILE_STORAGE_ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
