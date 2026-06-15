package com.nightguy.spark.comment;

import com.nightguy.spark.user.User;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts/{postId}/comments")
@AllArgsConstructor
public class CommentController {
  private final CommentService commentService;

  @PostMapping
  CommentResponseDTO createComment(
      @AuthenticationPrincipal User user,
      @RequestBody CommentRequestDTO newComment,
      @PathVariable Long postId) {
    System.out.println(user.getAuthorities());
    return commentService.createComment(user, newComment, postId);
  }

  @GetMapping
  Page<CommentResponseDTO> getAllComments(
      @PathVariable Long postId, @RequestParam(defaultValue = "0") int page) {
    return commentService.getAllCommentsForPost(postId, page);
  }

  @PutMapping("{commentId}")
  CommentResponseDTO updateComment(
      @AuthenticationPrincipal User user,
      @RequestBody CommentRequestDTO newComment,
      @PathVariable Long commentId) {
    return commentService.updateComment(user, newComment, commentId);
  }

  @DeleteMapping("{commentId}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteComment(@AuthenticationPrincipal User user, @PathVariable Long commentId) {
    commentService.deleteComment(user, commentId);
  }
}
