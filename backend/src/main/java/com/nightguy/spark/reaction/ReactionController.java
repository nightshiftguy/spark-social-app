package com.nightguy.spark.reaction;

import com.nightguy.spark.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/reactions")
@AllArgsConstructor
public class ReactionController {
  private final ReactionService reactionService;

  @PostMapping
  ReactionResponseDTO createReaction(
      @AuthenticationPrincipal User user,
      @RequestBody ReactionRequestDTO newReaction,
      @PathVariable Long postId) {
    return reactionService.saveReaction(user, newReaction, postId);
  }

  @GetMapping
  Page<ReactionResponseDTO> getAllReactions(
      @PathVariable Long postId, @RequestParam(defaultValue = "0") int page) {
    return reactionService.getAllReactionsForPost(postId, page);
  }

  @PutMapping
  ReactionResponseDTO updateReaction(
      @AuthenticationPrincipal User user,
      @RequestBody ReactionRequestDTO newReaction,
      @PathVariable Long postId) {
    return reactionService.updateReaction(user, newReaction, postId);
  }

  @DeleteMapping()
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteReaction(@AuthenticationPrincipal User user, @PathVariable Long postId) {
    reactionService.deleteReaction(user, postId);
  }
}
