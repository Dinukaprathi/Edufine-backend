package com.edufine.backend.mapper;

import com.edufine.backend.dto.DepartmentRequestDto;
import com.edufine.backend.dto.DepartmentResponseDto;
import com.edufine.backend.entity.Department;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DepartmentMapper {

    DepartmentMapper INSTANCE = Mappers.getMapper(DepartmentMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Department toEntity(DepartmentRequestDto requestDto);

    DepartmentResponseDto toResponseDto(Department department);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Department updateEntityFromDto(DepartmentRequestDto requestDto, @org.mapstruct.MappingTarget Department department);
}
