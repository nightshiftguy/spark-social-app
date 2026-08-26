package com.nightguy.spark.image.cloudinary;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import jakarta.annotation.PostConstruct;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class CloudinaryImagesService {

  @Value("${CLOUDINARY_CLOUD_NAME}")
  private String cloudName;

  @Value("${CLOUDINARY_API_KEY}")
  private String apiKey;

  @Value("${CLOUDINARY_API_SECRET}")
  private String apiSecret;

  @Value("${CLOUD_UPLOAD_FOLDER}")
  private String uploadFolder = "images";

  private Cloudinary cloudinary;

  @PostConstruct
  private void init() {
    cloudinary =
        new Cloudinary(
            ObjectUtils.asMap(
                "cloud_name", cloudName,
                "api_key", apiKey,
                "api_secret", apiSecret));
  }

  public String saveImage(UUID publicId, MultipartFile image) throws IOException {
    Map<String, Object> uploadParameters = new HashMap<>();
    uploadParameters.put("folder", uploadFolder);
    uploadParameters.put("public_id", publicId.toString());
    uploadParameters.put("resource_type", "image");
    uploadParameters.put("use_filename", true);
    uploadParameters.put("unique_filename", true);
    uploadParameters.put("overwrite", false);
    Map uploadResult = cloudinary.uploader().upload(image.getBytes(), uploadParameters);
    return (String) uploadResult.get("url");
  }

  protected void cloudinaryDeleteImage(UUID publicId) throws IOException {
    cloudinary.uploader().destroy(uploadFolder + "/" + publicId.toString(), ObjectUtils.emptyMap());
  }

  public String updateImage(UUID publicId, MultipartFile image) throws IOException {
    Map<String, Object> uploadParameters = new HashMap<>();
    uploadParameters.put("folder", uploadFolder);
    uploadParameters.put("public_id", publicId.toString());
    uploadParameters.put("resource_type", "image");
    uploadParameters.put("use_filename", true);
    uploadParameters.put("unique_filename", true);
    uploadParameters.put("overwrite", true);
    Map uploadResult = cloudinary.uploader().upload(image.getBytes(), uploadParameters);
    return (String) uploadResult.get("url");
  }
}
