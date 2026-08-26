package com.nightguy.spark.post;

import com.nightguy.spark.user.User;
import jakarta.validation.constraints.NotBlank;
import java.io.IOException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/posts")
@AllArgsConstructor
public class PostController {
  private final PostService postService;

  @PostMapping
  public PostResponseDTO createPost(
      @RequestParam("textContent") @NotBlank String textContent,
      @RequestParam(value = "image", required = false) MultipartFile image,
      @AuthenticationPrincipal User user)
      throws IOException {
    return postService.save(user, textContent, image);
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
      @AuthenticationPrincipal User user,
      @RequestParam("textContent") String textContent,
      @PathVariable Long id,
      @RequestParam(value = "image", required = false) MultipartFile image)
      throws IOException {
    return postService.updatePost(user, id, textContent, image);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deletePost(@AuthenticationPrincipal User user, @PathVariable Long id) {
    postService.deletePost(user, id);
  }
}
