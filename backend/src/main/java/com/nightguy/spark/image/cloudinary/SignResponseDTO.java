package com.nightguy.spark.image.cloudinary;

public record SignResponseDTO(
    String cloudUrl,
    String signature,
    String timestamp,
    String apiKey,
    String folder,
    String publicId,
    String uploadPreset) {}
