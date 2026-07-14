package com.nightguy.spark.image.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.signing.NotificationRequestSignatureVerifier;
import com.cloudinary.utils.ObjectUtils;
import com.nightguy.spark.image.ImageUrl;
import com.nightguy.spark.image.ImageUrlRepository;
import com.nightguy.spark.post.Post;
import com.nightguy.spark.post.PostRepository;
import com.nightguy.spark.user.User;
import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
public class CloudinaryImagesService {

  @Value("${CLOUDINARY_CLOUD_NAME}")
  private String cloudName;

  @Value("${CLOUDINARY_API_KEY}")
  private String apiKey;

  @Value("${CLOUDINARY_API_SECRET}")
  private String apiSecret;

  @Value("${CLOUD_UPLOAD_LOCATION}")
  private String cloudUploadLocation;

  private static final String UPLOAD_FOLDER = "images";
  private static final String UPLOAD_PRESET = "public-images";

  @Value("${CLOUD_MAX_TIME_FOR_IMAGE_CONFIRMATION_SECONDS}")
  private long secondsValidFor;

  private Cloudinary cloudinary;
  private NotificationRequestSignatureVerifier verifier;

  private final PostRepository postRepository;
  private final ImageUrlRepository imageUrlRepository;

  @PostConstruct
  private void init() {
    cloudinary =
        new Cloudinary(
            ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
    verifier = new NotificationRequestSignatureVerifier(apiSecret);
  }

  private SignResponseDTO signRequest(UUID publicId) {
    Map<String, Object> signParameters = new HashMap<>();
    long timestampUnixTime = System.currentTimeMillis() / 1000L;
    signParameters.put("folder", UPLOAD_FOLDER);
    signParameters.put("timestamp", timestampUnixTime);
    signParameters.put("upload_preset", UPLOAD_PRESET);
    signParameters.put("public_id", publicId.toString());
    String signature = cloudinary.apiSignRequest(signParameters, apiSecret, 1);

    return new SignResponseDTO(
        cloudUploadLocation,
        signature,
        Long.toString(timestampUnixTime),
        apiKey,
        UPLOAD_FOLDER,
        publicId.toString(),
        UPLOAD_PRESET);
  }

  private void cloudinaryDeleteImage(UUID publicId) throws IOException {
    cloudinary.uploader().destroy(UPLOAD_FOLDER+"/"+publicId.toString(), ObjectUtils.emptyMap());
  }

  //sign methods: post and put
  public SignResponseDTO signUploadRequest(User user, Long postId) {
    // check if post exists
    Post post =
        postRepository
            .findById(postId)
            .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

    // check if user owns post (has permission to edit is)
    if (!post.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // save image with empty URL
    UUID publicId = UUID.randomUUID();
    ImageUrl imageUrl = new ImageUrl();
    imageUrl.setOwner(user);
    imageUrl.setPost(post);
    imageUrl.setPublicId(publicId);
    imageUrlRepository.save(imageUrl);

    // sign request
    return signRequest(publicId);
  }

  //sign methods: post and put
  public SignResponseDTO signUploadRequest(User user) {
    // save image with empty URL
    UUID publicId = UUID.randomUUID();
    ImageUrl imageUrl = new ImageUrl();
    imageUrl.setOwner(user);
    imageUrl.setPublicId(publicId);
    imageUrlRepository.save(imageUrl);

    // sign request
    return signRequest(publicId);
  }

  private boolean isConfirmationRequestValid(
      String requestBody, String timestamp, String signature) {
    return verifier.verifySignature(requestBody, timestamp, signature, secondsValidFor);
  }

  public void createAfterConfirmation(
      ConfirmationRequestDTO request, String rawBody, String timestamp, String signature) {

    // validate request
    if (!isConfirmationRequestValid(rawBody, timestamp, signature)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid signature");
    }

    // check if image exists
    // convert publicId (foldername/<UUID>) to just UUID: delete foldername.length (+1 to delete /)
    UUID publicId = UUID.fromString(request.public_id().substring(UPLOAD_FOLDER.length() + 1));
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
    // check if post exists
    Post post =
            postRepository
                    .findById(postId)
                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post not found"));

    // check if user owns post (has permission to edit is)
    if (!post.getAuthor().equals(user)) {
      throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    // check if post has image
    if (post.getImageLink() == null) {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This post don't have image to delete");
    }

    //delete image from cloudinary
    UUID publicId = post.getImageLink().getPublicId();
    try {
      cloudinaryDeleteImage(publicId);
    } catch (IOException e) {
      throw new ResponseStatusException(HttpStatus.BAD_GATEWAY);
    }

    //delete image link from db
    post.setImageLink(null);
    postRepository.save(post);
  }
}
