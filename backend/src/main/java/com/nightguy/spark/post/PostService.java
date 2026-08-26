package com.nightguy.spark.post;

import com.nightguy.spark.image.ImageUrl;
import com.nightguy.spark.image.ImageUrlRepository;
import com.nightguy.spark.image.cloudinary.ImagesService;
import com.nightguy.spark.user.User;
import java.io.IOException;
import java.util.Arrays;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@AllArgsConstructor
public class PostService {
  // TODO try to optimize to avoid N + 1 database requests (finding and then deleting)
  private final PostRepository postRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final PostMapper postMapper;
  private final ImagesService imagesService;

  private ImageUrl findImageForUser(User user, String imageUrl) {
    ImageUrl image =
        imageUrlRepository
            .findByImageLink(imageUrl)
            .orElseThrow(
                () ->
                    new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Image with this url don't exist"));

    // check if image belongs to user
    if (!image.getOwner().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid image url");
    }
    return image;
  }

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

  @Transactional
  public PostResponseDTO save(User user, String textContent, MultipartFile image)
      throws IOException {
    Post newPost = new Post();
    newPost.setTextContent(textContent);
    newPost.setAuthor(user);
    if (image != null && !image.isEmpty()) {
      ImageUrl newImage = imagesService.createImage(image, user);
      // update image
      newImage.setPost(newPost);
      newPost.setImageLink(newImage);
    }

    Post postResponse = postRepository.save(newPost);
    return postMapper.toDto(postResponse);
  }

  @Transactional
  public PostResponseDTO updatePost(User user, Long id, String textContent, MultipartFile image)
      throws IOException {
    // find post
    Post post =
        postRepository
            .findById(id)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));
    // throw error if user doesn't own the post
    if (!post.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // modify post
    if (textContent != null && !textContent.isEmpty()) {
      post.setTextContent(textContent);
    }

    if (image != null && !image.isEmpty()) {
      // update image
      if (post.getImageLink() != null) {
        imagesService.updateImage(id, image);
      } else {
        ImageUrl newImage = imagesService.createImage(image, user);
        // update image
        newImage.setPost(post);
        post.setImageLink(newImage);
      }
    }

    return postMapper.toDto(postRepository.save(post));
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
