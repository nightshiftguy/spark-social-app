package com.nightguy.spark.image;

import com.nightguy.spark.image.cloudinary.CloudinaryImagesService;
import com.nightguy.spark.image.cloudinary.SignResponseDTO;
import com.nightguy.spark.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class ImageController {
  private final CloudinaryImagesService imageService;

  @GetMapping("posts/{postId}/image/sign")
  SignResponseDTO signRequest(@AuthenticationPrincipal User user, @PathVariable long postId) {
    return imageService.signUploadRequest(user, postId);
  }

  @GetMapping("images/sign")
  SignResponseDTO signRequest(@AuthenticationPrincipal User user) {
    return imageService.signUploadRequest(user);
  }

  @DeleteMapping("posts/{postId}/image")
  void deleteImage(@AuthenticationPrincipal User user, @PathVariable long postId){
     imageService.deleteImage(user, postId);
  }
}
