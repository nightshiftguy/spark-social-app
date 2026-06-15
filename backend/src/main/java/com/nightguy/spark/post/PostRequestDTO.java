package com.nightguy.spark.post;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PostRequestDTO(
    @NotBlank @Size(max = 300) String textContent, @Size(max = 300) String imageLink) {}
