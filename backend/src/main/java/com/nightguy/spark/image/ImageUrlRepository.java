package com.nightguy.spark.image;

import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
  Optional<ImageUrl> findByPublicId(UUID publicId);

  Optional<ImageUrl> findByImageLink(String imageLink);
}
