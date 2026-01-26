package com.edufine.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ModuleWithLessonsDto extends ModuleResponseDto {

    private List<LessonResponseDto> lessons;

    public ModuleWithLessonsDto() {}

    public ModuleWithLessonsDto(String id, String courseId, String code, String name, Integer semester,
                                Integer credits, String lecturerId, Boolean isActive,
                                LocalDateTime createdAt, LocalDateTime updatedAt,
                                List<LessonResponseDto> lessons) {
        super(id, courseId, code, name, semester, credits, lecturerId, isActive, createdAt, updatedAt);
        this.lessons = lessons;
    }

    public List<LessonResponseDto> getLessons() {
        return lessons;
    }

    public void setLessons(List<LessonResponseDto> lessons) {
        this.lessons = lessons;
    }
}
