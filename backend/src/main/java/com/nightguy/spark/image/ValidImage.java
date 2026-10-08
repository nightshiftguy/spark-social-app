package com.nightguy.spark.image;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = ImageValidator.class)
@Target({FIELD, PARAMETER, RECORD_COMPONENT})
@Retention(RUNTIME)
public @interface ValidImage {
  String message() default "Image must be a JPEG or PNG up to 5 MB";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
