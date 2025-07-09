package com.project.backend354.utils.validation;

import com.project.backend354.exception.FileValidationException;
import org.springframework.web.multipart.MultipartFile;

public class EmptyFileValidationHandler extends FileValidationHandler {

    @Override
    protected void doValidate(MultipartFile file) {
        if (file.isEmpty()) {
            throw new FileValidationException("File cannot be empty");
        }
    }
}
