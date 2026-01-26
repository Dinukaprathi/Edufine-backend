package com.edufine.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public class ModuleRequestDto {

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Module code is required")
    @Size(min = 2, max = 20, message = "Module code must be between 2 and 20 characters")
    private String code;

    @NotBlank(message = "Module name is required")
    @Size(min = 2, max = 200, message = "Module name must be between 2 and 200 characters")
    private String name;

    @NotNull(message = "Semester is required")
    @Positive(message = "Semester must be a positive number")
    private Integer semester;

    @NotNull(message = "Credits are required")
    @Positive(message = "Credits must be a positive number")
    private Integer credits;

    private String lecturerId;

    // Constructors
    public ModuleRequestDto() {}

    public ModuleRequestDto(String courseId, String code, String name, Integer semester, Integer credits, String lecturerId) {
        this.courseId = courseId;
        this.code = code;
        this.name = name;
        this.semester = semester;
        this.credits = credits;
        this.lecturerId = lecturerId;
    }

    // Getters and Setters
    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSemester() {
        return semester;
    }

    public void setSemester(Integer semester) {
        this.semester = semester;
    }

    public Integer getCredits() {
        return credits;
    }

    public void setCredits(Integer credits) {
        this.credits = credits;
    }

    public String getLecturerId() {
        return lecturerId;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }
}
