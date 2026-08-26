package com.nightguy.spark.image;

import com.nightguy.spark.image.cloudinary.ImagesService;
import com.nightguy.spark.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1/")
@RequiredArgsConstructor
public class ImageController {
  private final ImagesService imageService;

  @DeleteMapping("posts/{postId}/image")
  void deleteImage(@AuthenticationPrincipal User user, @PathVariable long postId) {
    imageService.deleteImage(user, postId);
  }
}
