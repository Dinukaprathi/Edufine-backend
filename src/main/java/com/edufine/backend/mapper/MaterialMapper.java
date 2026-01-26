package com.edufine.backend.mapper;

import com.edufine.backend.dto.MaterialDto;
import com.edufine.backend.entity.Material;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface MaterialMapper {

    MaterialMapper INSTANCE = Mappers.getMapper(MaterialMapper.class);

    MaterialDto toResponseDto(Material material);

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
