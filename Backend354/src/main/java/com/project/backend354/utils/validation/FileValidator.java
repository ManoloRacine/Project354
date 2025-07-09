package com.project.backend354.utils.validation;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class FileValidator {

    private final FileSizeValidationHandler fileSizeValidationHandler;

    public void validateImage(MultipartFile file) {
        FileValidationHandler[] handlers = {
                new EmptyFileValidationHandler(),
                fileSizeValidationHandler,
                new FileTypeValidationHandler(),
                new FilenameValidationHandler()
        };

        for (int i = 0; i < handlers.length - 1; i++) {
            handlers[i].setNext(handlers[i + 1]);
        }

        handlers[0].validate(file);
    }
}