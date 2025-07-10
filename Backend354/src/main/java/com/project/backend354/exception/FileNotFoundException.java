package com.project.backend354.exception;

import org.springframework.http.HttpStatus;

public class FileNotFoundException extends BaseException {

  public FileNotFoundException(String message) {
    super(message, "FILE_NOT_FOUND", HttpStatus.NOT_FOUND);
  }

  public FileNotFoundException(String message, Throwable cause) {
    super(message, cause, "FILE_NOT_FOUND", HttpStatus.NOT_FOUND);
  }
}
