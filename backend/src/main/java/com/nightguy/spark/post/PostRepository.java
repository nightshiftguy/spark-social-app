package com.nightguy.spark.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface PostRepository extends JpaRepository<Post, Long> {
  @Modifying
  @Query("UPDATE Post p set p.likeCount = p.likeCount + 1 WHERE p.id = ?1")
  void incrementLikeCount(Long postId);

  @Modifying
  @Query("UPDATE Post p set p.likeCount = p.likeCount - 1 WHERE p.id = ?1")
  void decrementLikeCount(Long postId);
}
