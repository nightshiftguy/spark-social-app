package com.nightguy.spark.post;

import com.nightguy.spark.image.ValidImage;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

public record PostRequestDTO(
    @NotBlank @Size(max = 300) String textContent, @ValidImage MultipartFile image) {}
