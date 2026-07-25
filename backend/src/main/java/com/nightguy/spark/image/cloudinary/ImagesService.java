package com.nightguy.spark.image.cloudinary;

import com.nightguy.spark.image.ImageUrl;
import com.nightguy.spark.image.ImageUrlRepository;
import com.nightguy.spark.post.Post;
import com.nightguy.spark.post.PostRepository;
import com.nightguy.spark.user.User;
import java.io.IOException;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ImagesService {

  private final PostRepository postRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final CloudinaryImagesService cloudinaryImagesService;

  private Post checkIfPostExistsAndUserOwnsIt(User user, Long postId) {
    // check if post exists
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

    // check if user owns post (has permission to edit is)
    if (!post.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }
    return post;
  }

  // sign methods: post and put
  public SignResponseDTO signUploadRequest(User user, Long postId) {
    Post post = checkIfPostExistsAndUserOwnsIt(user, postId);

    // save image with empty URL
    UUID publicId = UUID.randomUUID();
    ImageUrl imageUrl = new ImageUrl();
    imageUrl.setOwner(user);
    imageUrl.setPost(post);
    imageUrl.setPublicId(publicId);
    imageUrlRepository.save(imageUrl);

    // sign request
    return cloudinaryImagesService.signRequest(publicId);
  }

  // sign methods: post and put
  public SignResponseDTO signUploadRequest(User user) {
    // save image with empty URL
    UUID publicId = UUID.randomUUID();
    ImageUrl imageUrl = new ImageUrl();
    imageUrl.setOwner(user);
    imageUrl.setPublicId(publicId);
    imageUrlRepository.save(imageUrl);

    // sign request
    return cloudinaryImagesService.signRequest(publicId);
  }

  public void createAfterConfirmation(
      ConfirmationRequestDTO request, String rawBody, String timestamp, String signature) {

    // validate request
    if (!cloudinaryImagesService.isConfirmationRequestValid(rawBody, timestamp, signature)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid signature");
    }

    // check if image exists
    UUID publicId = cloudinaryImagesService.getUUIDFromRequestString(request.public_id());
    ImageUrl image =
        imageUrlRepository
            .findByPublicId(publicId)
            .orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Image not found"));

    // update it's url if empty
    if (image.getImageLink() != null) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Image has been already uploaded");
    }
    image.setImageLink(request.url());
    imageUrlRepository.save(image);
  }

  public void deleteImage(User user, long postId) {
    Post post = checkIfPostExistsAndUserOwnsIt(user, postId);

    // check if post has image
    if (post.getImageLink() == null) {
      throw new ResponseStatusException(
          HttpStatus.NOT_FOUND, "This post don't have image to delete");
    }

    // delete image from cloudinary
    UUID publicId = post.getImageLink().getPublicId();
    try {
      cloudinaryImagesService.cloudinaryDeleteImage(publicId);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
    }

    // delete image link from db
    post.setImageLink(null);
    postRepository.save(post);
  }
}
