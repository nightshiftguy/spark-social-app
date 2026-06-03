package com.nightguy.spark.user;

import com.nightguy.spark.reaction.ReactionResponseDTO;
import com.nightguy.spark.reaction.ReactionService;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/users/me")
@AllArgsConstructor
public class CurrentUserController {
  private final ReactionService reactionService;

  @GetMapping("/reactions")
  List<ReactionResponseDTO> getReactions(@AuthenticationPrincipal User user) {
    return reactionService.getAllReactionsForUser(user);
  }

  @GetMapping("/reactions/")
  ReactionResponseDTO getReaction(@AuthenticationPrincipal User user, @RequestParam Long postId) {
    return reactionService.getUsersReactionForPost(user, postId);
  }
}
