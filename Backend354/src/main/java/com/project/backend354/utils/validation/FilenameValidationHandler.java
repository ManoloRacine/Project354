package com.project.backend354.utils.validation;

import com.project.backend354.exception.FileValidationException;
import org.springframework.web.multipart.MultipartFile;

public class FilenameValidationHandler extends FileValidationHandler {

    @Override
    protected void doValidate(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null || filename.trim().isEmpty()) {
            throw new FileValidationException("Filename cannot be empty");
        }
    }
}
