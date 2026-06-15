package com.nightguy.spark.post;

import com.nightguy.spark.user.User;
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
  PostResponseDTO createPost(
      @AuthenticationPrincipal User user, @RequestBody PostRequestDTO newPost) {
    return postService.save(user, newPost);
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

  @PutMapping("/{id}")
  PostResponseDTO updatePost(
      @AuthenticationPrincipal User user,
      @RequestBody PostRequestDTO newPost,
      @PathVariable Long id) {
    return postService.updatePost(user, id, newPost);
  }

  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deletePost(@AuthenticationPrincipal User user, @PathVariable Long id) {
    postService.deletePost(user, id);
  }
}
