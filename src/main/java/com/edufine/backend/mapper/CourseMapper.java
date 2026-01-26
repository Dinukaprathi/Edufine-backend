package com.edufine.backend.mapper;

import com.edufine.backend.dto.CourseResponseDto;
import com.edufine.backend.entity.Course;
import org.bson.types.ObjectId;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseResponseDto toResponseDto(Course course);

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
