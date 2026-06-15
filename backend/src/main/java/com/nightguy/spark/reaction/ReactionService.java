package com.nightguy.spark.reaction;

import com.nightguy.spark.post.Post;
import com.nightguy.spark.post.PostRepository;
import com.nightguy.spark.user.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class ReactionService {
  private final ReactionRepository reactionRepository;
  private final PostRepository postRepository;
  private final ReactionMapper reactionMapper;
  @PersistenceContext private EntityManager em;

  @Transactional
  public ReactionResponseDTO saveReaction(
      User user, @Valid ReactionRequestDTO newReactionDto, Long postId) {
    // prevent user from reacting twice to same post
    if (reactionRepository.findByAuthorAndPost_Id(user, postId).isPresent()) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    Reaction newReaction = reactionMapper.toEntity(newReactionDto);
    newReaction.setPost(em.getReference(Post.class, postId));
    newReaction.setAuthor(user);

    // save reaction
    ReactionResponseDTO responseDTO = reactionMapper.toDto(reactionRepository.save(newReaction));
    // update reaction count after reacting
    postRepository.incrementLikeCount(postId);

    return responseDTO;
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

  public Page<ReactionResponseDTO> getAllReactionsForPost(Long postId, int pageNumber) {
    if (pageNumber < 0) throw new IllegalArgumentException("Invalid request param pageNumber");
    // sort by author
    Sort sort = Sort.by(Sort.Direction.ASC, "author");
    Pageable pageable = PageRequest.of(pageNumber, 10, sort);
    return reactionRepository.findAllByPost_Id(postId, pageable).map(reactionMapper::toDto);
  }

  public Page<ReactionResponseDTO> getAllReactionsForUser(User user, int pageNumber) {
    if (pageNumber < 0) throw new IllegalArgumentException("Invalid request param pageNumber");
    // sort from latest to oldest
    Sort sort = Sort.by(Sort.Direction.DESC, "creationTimestamp");
    Pageable pageable = PageRequest.of(pageNumber, 10, sort);
    return reactionRepository.findAllByAuthor(user, pageable).map(reactionMapper::toDto);
  }

  public ReactionResponseDTO getUsersReactionForPost(User user, Long postId) {
    Reaction newReaction =
        reactionRepository
            .findByAuthorAndPost_Id(user, postId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Reaction not found"));
    return reactionMapper.toDto(newReaction);
  }

  @Transactional
  public void deleteReaction(User user, Long postId) {
    reactionRepository.removeByPostIdAndAuthor(postId, user);
    postRepository.decrementLikeCount(postId);
  }
}
