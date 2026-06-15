package com.nightguy.spark.reaction;

import jakarta.validation.constraints.NotNull;

public record ReactionRequestDTO(@NotNull ReactionType reaction) {}
