package com.nightguy.spark.reaction;

import com.nightguy.spark.user.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {

  @Query("SELECT r FROM Reaction r JOIN FETCH r.author WHERE r.post.id = :postId")
  Page<Reaction> findAllByPostId(Long postId, Pageable pageable);

  Page<Reaction> findAllByAuthor(User author, Pageable pageable);

  Optional<Reaction> findByAuthorAndPost_Id(User author, Long postId);

  void removeByPostIdAndAuthor(Long postId, User user);
}
