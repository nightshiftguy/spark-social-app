package com.nightguy.spark.reaction;

import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface ReactionMapper {
  Reaction toEntity(ReactionRequestDTO dto);

  ReactionResponseDTO toDto(Reaction entity);

  Reaction updateEntityFromDto(@MappingTarget Reaction entity, ReactionRequestDTO dto);
}
