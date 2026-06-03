package com.nightguy.spark.reaction;

import com.nightguy.spark.user.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReactionRepository extends JpaRepository<Reaction, Long> {
  List<Reaction> findAllByPost_Id(Long postId);

  List<Reaction> findAllByAuthor(User author);

  Optional<Reaction> findByAuthorAndPost_Id(User author, Long postId);

  void removeByIdAndAuthor(Long reactionId, User author);
}
