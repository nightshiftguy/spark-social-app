package com.nightguy.spark.post;

import com.nightguy.spark.user.User;
import jakarta.validation.Valid;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class PostService {
  // TODO try to optimize to avoid N + 1 database requests (finding and then deleting)
  private final PostRepository postRepository;
  private final PostMapper postMapper;

  public List<PostResponseDTO> getAllPosts() {
    return postRepository.findAll().stream().map(postMapper::toDto).toList();
  }

  public PostResponseDTO getPost(Long id) {
    Post newPost =
        postRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    return postMapper.toDto(newPost);
  }

  public PostResponseDTO save(User user, @Valid PostRequestDTO newPostDto) {
    Post newPost = postMapper.toEntity(newPostDto);
    newPost.setAuthor(user);
    Post postResponse = postRepository.save(newPost);
    return postMapper.toDto(postResponse);
  }

  public PostResponseDTO updatePost(User user, Long id, @Valid PostRequestDTO newPostDto) {
    // find post
    Post oldPost =
        postRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    // throw error if user doesn't own the post
    if (!oldPost.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    // modify post
    Post postEntity = postMapper.updateEntityFromDto(oldPost, newPostDto);
    return postMapper.toDto(postRepository.save(postEntity));
  }

  public void deletePost(User user, Long id) {
    // find post
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    // throw error if user doesn't own the post
    if (!post.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    // delete post
    postRepository.deleteById(id);
  }
}
