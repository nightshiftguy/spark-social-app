package com.nightguy.spark.image;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import javax.imageio.ImageIO;
import org.springframework.web.multipart.MultipartFile;

public class ImageValidator implements ConstraintValidator<ValidImage, MultipartFile> {

  private static final long MAX_SIZE = 5 * 1024 * 1024;
  private static final Set<String> ALLOWED_TYPES = Set.of("image/jpeg", "image/png");

  @Override
  public boolean isValid(MultipartFile file, ConstraintValidatorContext context) {
    if (file == null || file.isEmpty()) {
      return true;
    }
    if (file.getSize() > MAX_SIZE) {
      return false;
    }
    if (!ALLOWED_TYPES.contains(file.getContentType())) {
      return false;
    }
    try (InputStream in = file.getInputStream()) {
      return ImageIO.read(in) != null;
    } catch (IOException e) {
      return false;
    }
  }
}
