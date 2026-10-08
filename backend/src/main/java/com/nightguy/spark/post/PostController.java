package com.nightguy.spark.post;

import com.nightguy.spark.user.User;
import jakarta.validation.Valid;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {
  private final PostService postService;

  @PostMapping
  public PostResponseDTO createPost(
      @Valid @ModelAttribute PostRequestDTO newPost, @AuthenticationPrincipal User user)
      throws IOException {
    return postService.save(user, newPost.textContent(), newPost.image());
  }

  @GetMapping
  Page<PostResponseDTO> getPosts(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "creationTimestamp") String sortBy,
      @RequestParam(defaultValue = "DESC") String sortDirection) {
    return postService.getAllPosts(page, sortBy, sortDirection);
  }

  @GetMapping("/{id}")
  PostResponseDTO getPost(@PathVariable Long id) {
    return postService.getPost(id);
  }

  @PatchMapping("/{id}")
  PostResponseDTO updatePost(
      @Valid @ModelAttribute PostRequestDTO newPost,
      @AuthenticationPrincipal User user,
      @PathVariable Long id)
      throws IOException {
    return postService.updatePost(user, id, newPost.textContent(), newPost.image());
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deletePost(@AuthenticationPrincipal User user, @PathVariable Long id) {
    postService.deletePost(user, id);
  }
}
