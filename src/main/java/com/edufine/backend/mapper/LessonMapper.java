package com.edufine.backend.mapper;

import com.edufine.backend.dto.LessonResponseDto;
import com.edufine.backend.entity.Lesson;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LessonMapper {

    LessonMapper INSTANCE = Mappers.getMapper(LessonMapper.class);

    LessonResponseDto toResponseDto(Lesson lesson);

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
