package com.nightguy.spark.post;

import com.nightguy.spark.user.UserBasicInfoDTO;
import java.time.Instant;

public record PostResponseDTO(
    Long id,
    UserBasicInfoDTO author,
    String textContent,
    String imageLink,
    Integer likeCount,
    Instant creationTimestamp) {}
