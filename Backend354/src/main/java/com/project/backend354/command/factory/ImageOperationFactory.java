package com.project.backend354.command.factory;

import com.project.backend354.command.*;
import com.project.backend354.exception.ImageProcessingException;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ImageOperationFactory {

    public ImageOperation createOperation(String type, String inputPath, String outputPath, Map<String, Object> config) {
        return switch (type.toLowerCase()) {
            case "resize" -> createResizeOperation(
                    inputPath, outputPath,
                    (Integer) config.get("width"),
                    (Integer) config.get("height")
            );
            case "crop" -> createCropOperation(
                    inputPath, outputPath,
                    (Integer) config.get("width"),
                    (Integer) config.get("height"),
                    (Integer) config.get("x"),
                    (Integer) config.get("y")
            );
            case "rotate" -> createRotateOperation(
                    inputPath, outputPath,
                    ((Number) config.get("angle")).doubleValue()
            );
            case "flip" -> createFlipOperation(
                    inputPath, outputPath,
                    (Boolean) config.get("horizontal"),
                    (Boolean) config.get("vertical")
            );
            case "quality" -> createQualityOperation(
                    inputPath, outputPath,
                    (Integer) config.get("quality")
            );
            case "brightness-contrast" -> createBrightnessContrastOperation(
                    inputPath, outputPath,
                    (Integer) config.get("brightness"),
                    (Integer) config.get("contrast")
            );
            case "format" -> createFormatConvertOperation(
                    inputPath, outputPath,
                    (String) config.get("format")
            );
            default -> throw new IllegalArgumentException("Unknown operation type: " + type);
        };
    }

    public ResizeOperation createResizeOperation(String inputPath, String outputPath, int width, int height) {
        validatePaths(inputPath, outputPath);
        validateDimensions(width, height);
        return new ResizeOperation(inputPath, outputPath, width, height);
    }

    public CropOperation createCropOperation(String inputPath, String outputPath, int width, int height, int x, int y) {
        validatePaths(inputPath, outputPath);
        validateDimensions(width, height);
        validateCoordinates(x, y);
        return new CropOperation(inputPath, outputPath, width, height, x, y);
    }

    public RotateOperation createRotateOperation(String inputPath, String outputPath, double angle) {
        validatePaths(inputPath, outputPath);
        return new RotateOperation(inputPath, outputPath, angle);
    }

    public FlipOperation createFlipOperation(String inputPath, String outputPath, boolean horizontal, boolean vertical) {
        validatePaths(inputPath, outputPath);
        if (!horizontal && !vertical) {
            throw new ImageProcessingException("At least one flip direction must be specified");
        }
        return new FlipOperation(inputPath, outputPath, horizontal, vertical);
    }

    public QualityOperation createQualityOperation(String inputPath, String outputPath, int quality) {
        validatePaths(inputPath, outputPath);
        if (quality < 1 || quality > 100) {
            throw new ImageProcessingException("Quality must be between 1 and 100");
        }
        return new QualityOperation(inputPath, outputPath, quality);
    }

    public BrightnessContrastOperation createBrightnessContrastOperation(String inputPath, String outputPath, int brightness, int contrast) {
        validatePaths(inputPath, outputPath);
        if (brightness < -100 || brightness > 100) {
            throw new ImageProcessingException("Brightness must be between -100 and 100");
        }
        if (contrast < -100 || contrast > 100) {
            throw new ImageProcessingException("Contrast must be between -100 and 100");
        }
        return new BrightnessContrastOperation(inputPath, outputPath, brightness, contrast);
    }

    public FormatConvertOperation createFormatConvertOperation(String inputPath, String outputPath, String format) {
        validatePaths(inputPath, outputPath);
        if (format == null || format.trim().isEmpty()) {
            throw new ImageProcessingException("Format cannot be empty");
        }
        return new FormatConvertOperation(inputPath, outputPath, format);
    }

    private void validatePaths(String inputPath, String outputPath) {
        if (inputPath == null || inputPath.trim().isEmpty()) {
            throw new ImageProcessingException("Input path cannot be empty");
        }
        if (outputPath == null || outputPath.trim().isEmpty()) {
            throw new ImageProcessingException("Output path cannot be empty");
        }
    }

    private void validateDimensions(int width, int height) {
        if (width <= 0 || height <= 0) {
            throw new ImageProcessingException("Width and height must be positive values");
        }
    }

    private void validateCoordinates(int x, int y) {
        if (x < 0 || y < 0) {
            throw new ImageProcessingException("Coordinates must be non-negative values");
        }
    }
}
