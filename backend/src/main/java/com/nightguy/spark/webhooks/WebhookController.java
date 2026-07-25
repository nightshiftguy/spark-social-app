package com.nightguy.spark.webhooks;

import com.nightguy.spark.image.cloudinary.ConfirmationRequestDTO;
import com.nightguy.spark.image.cloudinary.ImagesService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tools.jackson.databind.json.JsonMapper;

@RestController
@RequestMapping("/webhooks")
@RequiredArgsConstructor
public class WebhookController {
  private final ImagesService imageService;
  private final JsonMapper jsonMapper;

  @RequestMapping(
      value = "/cloudinary",
      method = {RequestMethod.POST})
  @ResponseStatus(HttpStatus.OK)
  void postUploadConfirmation(
      @RequestBody String rawBody,
      @RequestHeader(value = "X-Cld-Timestamp") String timestamp,
      @RequestHeader(value = "X-Cld-Signature") String signature) {
    ConfirmationRequestDTO body = jsonMapper.readValue(rawBody, ConfirmationRequestDTO.class);
    imageService.createAfterConfirmation(body, rawBody, timestamp, signature);
  }
}
