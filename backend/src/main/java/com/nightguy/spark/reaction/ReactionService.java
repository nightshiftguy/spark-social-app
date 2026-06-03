package com.nightguy.spark.reaction;

import com.nightguy.spark.post.Post;
import com.nightguy.spark.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ReactionService {
  private final ReactionRepository reactionRepository;
  private final ReactionMapper reactionMapper;
  @PersistenceContext private EntityManager em;

  public ReactionResponseDTO saveReaction(
      User user, @Valid ReactionRequestDTO newReactionDto, Long postId) {
    // prevent user from reacting twice to same post
    if (reactionRepository.findByAuthorAndPost_Id(user, postId).isPresent()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    Reaction newReaction = reactionMapper.toEntity(newReactionDto);
    newReaction.setPost(em.getReference(Post.class, postId));
    newReaction.setAuthor(user);

    return reactionMapper.toDto(reactionRepository.save(newReaction));
  }

  public ReactionResponseDTO updateReaction(
      User user, @Valid ReactionRequestDTO newReactionDto, Long postId) {
    Reaction oldReaction =
        reactionRepository
            .findByAuthorAndPost_Id(user, postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reaction not found"));
    Reaction newReaction = reactionMapper.updateEntityFromDto(oldReaction, newReactionDto);
    return reactionMapper.toDto(reactionRepository.save(newReaction));
  }

  public List<ReactionResponseDTO> getAllReactionsForPost(Long postId) {
    return reactionRepository.findAllByPost_Id(postId).stream().map(reactionMapper::toDto).toList();
  }

  public List<ReactionResponseDTO> getAllReactionsForUser(User user) {
    return reactionRepository.findAllByAuthor(user).stream().map(reactionMapper::toDto).toList();
  }

  public ReactionResponseDTO getUsersReactionForPost(User user, Long postId) {
    Reaction newReaction =
        reactionRepository
            .findByAuthorAndPost_Id(user, postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reaction not found"));
    return reactionMapper.toDto(newReaction);
  }

  public void deleteReaction(User user, Long reactionId) {
    reactionRepository.removeByIdAndAuthor(reactionId, user);
  }
}
