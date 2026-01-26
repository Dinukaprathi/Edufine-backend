package com.edufine.backend.dto;

import java.time.LocalDateTime;
import java.util.List;

public class CourseResponseDto {

    private String id;
    private String code;
    private String name;
    private Integer durationInYears;
    private String description;
    private Integer totalCredits;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<String> moduleIds;
    private List<ModuleWithLessonsDto> modules;
    private List<SemesterModulesDto> semesters;

    public CourseResponseDto() {}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Integer getDurationInYears() {
        return durationInYears;
    }

    public void setDurationInYears(Integer durationInYears) {
        this.durationInYears = durationInYears;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getTotalCredits() {
        return totalCredits;
    }

    public void setTotalCredits(Integer totalCredits) {
        this.totalCredits = totalCredits;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public List<String> getModuleIds() {
        return moduleIds;
    }

    public void setModuleIds(List<String> moduleIds) {
        this.moduleIds = moduleIds;
    }

    public List<ModuleWithLessonsDto> getModules() {
        return modules;
    }

    public void setModules(List<ModuleWithLessonsDto> modules) {
        this.modules = modules;
    }

    public List<SemesterModulesDto> getSemesters() {
        return semesters;
    }

    public void setSemesters(List<SemesterModulesDto> semesters) {
        this.semesters = semesters;
    }
}
