package com.nightguy.spark.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
  @Query("SELECT c FROM Comment c JOIN FETCH c.author WHERE c.post.id = :postId")
  Page<Comment> findAllByPostId(Long postId, Pageable pageable);
}
