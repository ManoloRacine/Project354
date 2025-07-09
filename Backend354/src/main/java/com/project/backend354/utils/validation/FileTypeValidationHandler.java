package com.project.backend354.utils.validation;

import com.project.backend354.exception.FileValidationException;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

public class FileTypeValidationHandler extends FileValidationHandler {

    private static final Set<String> ALLOWED_TYPES = Set.of(
            MediaType.IMAGE_JPEG_VALUE,
            MediaType.IMAGE_PNG_VALUE,
            MediaType.IMAGE_GIF_VALUE,
            "image/webp" // MediaType doesn't include WebP by default
    );

    @Override
    protected void doValidate(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !ALLOWED_TYPES.contains(contentType.toLowerCase())) {
            throw new FileValidationException("Only JPEG, PNG, GIF, and WebP images are allowed");
        }
    }
}
