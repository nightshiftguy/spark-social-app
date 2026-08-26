package com.nightguy.spark.image;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUrlRepository extends JpaRepository<ImageUrl, Long> {
  Optional<ImageUrl> findByImageLink(String imageLink);

  Optional<ImageUrl> findByPostId(long postId);
}
