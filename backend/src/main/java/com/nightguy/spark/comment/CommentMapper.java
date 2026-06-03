package com.nightguy.spark.comment;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface CommentMapper {
  Comment toEntity(CommentRequestDTO dto);

  CommentResponseDTO toDto(Comment entity);

  Comment updateEntityFromDto(@MappingTarget Comment entity, CommentRequestDTO dto);
}
