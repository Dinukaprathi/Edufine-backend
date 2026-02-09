package com.edufine.backend.mapper;

import com.edufine.backend.dto.NoticeRequestDto;
import com.edufine.backend.dto.NoticeResponseDto;
import com.edufine.backend.entity.Notice;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface NoticeMapper {

    NoticeMapper INSTANCE = Mappers.getMapper(NoticeMapper.class);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Notice toEntity(NoticeRequestDto requestDto);

    @Mapping(target = "id", expression = "java(notice.getId() != null ? notice.getId().toHexString() : null)")
    NoticeResponseDto toResponseDto(Notice notice);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "active", ignore = true)
    @Mapping(target = "createdBy", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    Notice updateEntityFromDto(NoticeRequestDto requestDto, @org.mapstruct.MappingTarget Notice notice);
}
