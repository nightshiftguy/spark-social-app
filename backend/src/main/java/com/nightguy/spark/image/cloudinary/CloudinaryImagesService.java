package com.nightguy.spark.image.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.api.signing.NotificationRequestSignatureVerifier;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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

  @Value("${CLOUD_UPLOAD_FOLDER}")
  private String uploadFolder = "images";

  @Value("${CLOUD_UPLOAD_PRESET}")
  private String uploadPreset = "public-images";

  @Value("${CLOUD_MAX_TIME_FOR_IMAGE_CONFIRMATION_SECONDS}")
  private long secondsValidFor;

  private Cloudinary cloudinary;
  private NotificationRequestSignatureVerifier verifier;

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

  protected UUID getUUIDFromRequestString(String requestPublicId) {
    // convert publicId (foldername/<UUID>) to just UUID: delete foldername.length (+1 to delete /)
    return UUID.fromString(requestPublicId.substring(uploadFolder.length() + 1));
  }

  protected SignResponseDTO signRequest(UUID publicId) {
    Map<String, Object> signParameters = new HashMap<>();
    long timestampUnixTime = System.currentTimeMillis() / 1000L;
    signParameters.put("folder", uploadFolder);
    signParameters.put("timestamp", timestampUnixTime);
    signParameters.put("upload_preset", uploadPreset);
    signParameters.put("public_id", publicId.toString());
    String signature = cloudinary.apiSignRequest(signParameters, apiSecret, 1);

    return new SignResponseDTO(
        cloudUploadLocation,
        signature,
        Long.toString(timestampUnixTime),
        apiKey,
        uploadFolder,
        publicId.toString(),
        uploadPreset);
  }

  protected void cloudinaryDeleteImage(UUID publicId) throws IOException {
    cloudinary.uploader().destroy(uploadFolder + "/" + publicId.toString(), ObjectUtils.emptyMap());
  }

  protected boolean isConfirmationRequestValid(
      String requestBody, String timestamp, String signature) {
    return verifier.verifySignature(requestBody, timestamp, signature, secondsValidFor);
  }
}
