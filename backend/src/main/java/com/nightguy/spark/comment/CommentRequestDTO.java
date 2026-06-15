package com.nightguy.spark.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequestDTO(@NotBlank @Size(max = 200) String textContent) {}
