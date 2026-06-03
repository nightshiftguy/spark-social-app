package com.nightguy.spark.post;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface PostMapper {
  Post toEntity(PostRequestDTO dto);

  PostResponseDTO toDto(Post entity);

  Post updateEntityFromDto(@MappingTarget Post entity, PostRequestDTO dto);
}
