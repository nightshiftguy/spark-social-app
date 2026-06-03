package com.nightguy.spark.reaction;

import com.nightguy.spark.user.UserBasicInfoDTO;

public record ReactionResponseDTO(Long id, ReactionType reaction, UserBasicInfoDTO author) {}
