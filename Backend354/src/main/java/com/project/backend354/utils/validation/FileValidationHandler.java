package com.project.backend354.utils.validation;

import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

public abstract class FileValidationHandler {

    @Setter
    @Accessors(chain = true)
    protected FileValidationHandler next;

    public void validate(MultipartFile file) {
        doValidate(file);

        if (next != null) {
            next.validate(file);
        }
    }

    protected abstract void doValidate(MultipartFile file);
}
