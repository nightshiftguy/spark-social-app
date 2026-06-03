package com.nightguy.spark.comment;

import com.nightguy.spark.user.UserBasicInfoDTO;
import java.time.Instant;

public record CommentResponseDTO(
    Long id, String textContent, UserBasicInfoDTO author, Instant creationTimestamp) {}
