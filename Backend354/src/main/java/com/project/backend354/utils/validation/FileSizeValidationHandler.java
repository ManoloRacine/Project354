package com.project.backend354.utils.validation;

import com.project.backend354.exception.FileValidationException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.convert.DataSizeUnit;
import org.springframework.stereotype.Component;
import org.springframework.util.unit.DataSize;
import org.springframework.util.unit.DataUnit;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSizeValidationHandler extends FileValidationHandler {

    @Value("${upload.max-file-size}")
    @DataSizeUnit(DataUnit.BYTES)
    private DataSize maxFileSize;

    @Override
    protected void doValidate(MultipartFile file) {
        if (file.getSize() > maxFileSize.toBytes()) {
            throw new FileValidationException("File size exceeds the maximum allowed limit of " + maxFileSize.toBytes() + " bytes");
        }
    }
}
