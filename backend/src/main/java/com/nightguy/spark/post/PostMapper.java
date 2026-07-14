package com.nightguy.spark.post;

import com.nightguy.spark.image.ImageUrl;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
  @Mapping(target = "imageLink", ignore = true)
  Post toEntity(PostRequestDTO dto);

  PostResponseDTO toDto(Post entity);

  default String map(ImageUrl value) {
    return value != null
        ? value.getImageLink()
        : null; // adjust getter name to match your ImageUrl class
  }

  @Mapping(target = "imageLink", ignore = true)
  Post updateEntityFromDto(@MappingTarget Post entity, PostRequestDTO dto);
}
