package com.nightguy.spark.reaction;

import com.nightguy.spark.user.User;
import java.util.List;
import lombok.AllArgsConstructor;
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
  List<ReactionResponseDTO> getAllReactions(@PathVariable Long postId) {
    return reactionService.getAllReactionsForPost(postId);
  }

  @PutMapping
  ReactionResponseDTO updateReaction(
      @AuthenticationPrincipal User user,
      @RequestBody ReactionRequestDTO newReaction,
      @PathVariable Long postId) {
    return reactionService.updateReaction(user, newReaction, postId);
  }

  @DeleteMapping("{reactionId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteReaction(@AuthenticationPrincipal User user, @PathVariable Long reactionId) {
    reactionService.deleteReaction(user, reactionId);
  }
}
