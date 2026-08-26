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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class ImagesService {

  private final PostRepository postRepository;
  private final ImageUrlRepository imageUrlRepository;
  private final CloudinaryImagesService cloudinaryImagesService;

  private void validateImage(MultipartFile image) {
    if (image == null || image.isEmpty()) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Missing file");
    }

    // Simple server-side checks
    if (image.getSize() > 5 * 1024 * 1024) {
      throw new ResponseStatusException(HttpStatus.CONTENT_TOO_LARGE, "Max file size is 5 MB");
    }
    String contentType = image.getContentType();
    if (contentType == null || !contentType.startsWith("image/")) {
      throw new ResponseStatusException(
          HttpStatus.UNSUPPORTED_MEDIA_TYPE, "Only image uploads are allowed");
    }
  }

  public void updateImage(long postId, MultipartFile image) throws IOException {
    validateImage(image);
    // find image to edit
    ImageUrl imageUrl =
        imageUrlRepository
            .findByPostId(postId)
            .orElseThrow(
                () ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "Cannot find image to edit"));
    String imageLink = cloudinaryImagesService.updateImage(imageUrl.getPublicId(), image);
    imageUrl.setImageLink(imageLink);
    imageUrlRepository.save(imageUrl);
  }

  public ImageUrl createImage(MultipartFile image, User user) throws IOException {
    validateImage(image);

    // send image to cloud and get it's imageLink
    UUID publicId = UUID.randomUUID();
    String imageLink = cloudinaryImagesService.saveImage(publicId, image);

    // save image
    ImageUrl imageUrl = new ImageUrl();
    imageUrl.setImageLink(imageLink);
    imageUrl.setPublicId(publicId);
    imageUrl.setOwner(user);
    return imageUrlRepository.save(imageUrl);
  }

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
