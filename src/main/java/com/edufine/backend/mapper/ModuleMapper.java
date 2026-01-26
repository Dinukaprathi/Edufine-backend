package com.edufine.backend.mapper;

import com.edufine.backend.dto.ModuleRequestDto;
import com.edufine.backend.dto.ModuleResponseDto;
import com.edufine.backend.entity.Module;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ModuleMapper {

    ModuleMapper INSTANCE = Mappers.getMapper(ModuleMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Module toEntity(ModuleRequestDto requestDto);

    ModuleResponseDto toResponseDto(Module module);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "isActive", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Module updateEntityFromDto(ModuleRequestDto requestDto, @MappingTarget Module module);

    // Helpers for ObjectId <-> String mapping
    default String map(ObjectId value) {
        return value != null ? value.toHexString() : null;
    }

    default ObjectId map(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        return ObjectId.isValid(value) ? new ObjectId(value) : null;
    }
}
