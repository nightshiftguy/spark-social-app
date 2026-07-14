package com.nightguy.spark.post;

import com.nightguy.spark.image.ImageUrl;
import com.nightguy.spark.image.ImageUrlRepository;
import com.nightguy.spark.user.User;
import jakarta.validation.Valid;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class PostService {
  // TODO try to optimize to avoid N + 1 database requests (finding and then deleting)
  private final PostRepository postRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final PostMapper postMapper;

  public Page<PostResponseDTO> getAllPosts(int page, String sortBy, String sortDirection) {
    // validate parameters
    String[] validSortCategories = {"creationTimestamp", "likeCount"};
    String[] validSortDirection = {"asc", "desc"};
    if (page < 0) throw new IllegalArgumentException("Invalid request param page");
    if (Arrays.stream(validSortCategories).noneMatch((s) -> s.equals(sortBy))) {
      throw new IllegalArgumentException("Invalid request param sortBy");
    }
    if (Arrays.stream(validSortDirection).noneMatch((s) -> s.equalsIgnoreCase(sortDirection))) {
      throw new IllegalArgumentException("Invalid request param sortDirection");
    }

    Sort.Direction direction =
        sortDirection.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Sort sort = Sort.by(direction, sortBy);
    Pageable pageable = PageRequest.of(page, 10, sort);

    return postRepository.findAll(pageable).map(postMapper::toDto);
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

    //check if image specified in link exists and belongs to user
    if(newPostDto.imageLink() != null){
      ImageUrl image = imageUrlRepository.findByImageLink(newPostDto.imageLink())
              .orElseThrow(
                      ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image with this url don't exist")
              );

      if(
              !image.getOwner().equals(user) || //check if image belongs to user
              image.getPost() != null           //check if image isn't attached to post
      ){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid image url");
      }

      //update image
      image.setPost(newPost);
      newPost.setImageLink(image);
    }

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

    //check if image specified in link exists and belongs to user
    if(newPostDto.imageLink() != null){
      ImageUrl image = imageUrlRepository.findByImageLink(newPostDto.imageLink())
              .orElseThrow(
                      ()-> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image with this url don't exist")
              );
      //validate image
      if(
              !image.getOwner().equals(user) ||           //check if image belongs to user
              image.getPost() != null &&
              !Objects.equals(image.getPost().getId(), id) //check if post referred in image has same id as post to update
      ){
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid image url");
      }

      image.setPost(postEntity);
      postEntity.setImageLink(image);
    }

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
