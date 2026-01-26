package com.edufine.backend.dto;

import java.util.List;

public class SemesterModulesDto {

    private Integer semesterNumber;
    private List<String> moduleIds;
    private List<ModuleWithLessonsDto> modules;

    public SemesterModulesDto() {}

    public SemesterModulesDto(Integer semesterNumber, List<String> moduleIds, List<ModuleWithLessonsDto> modules) {
        this.semesterNumber = semesterNumber;
        this.moduleIds = moduleIds;
        this.modules = modules;
    }

    public Integer getSemesterNumber() {
        return semesterNumber;
    }

    public void setSemesterNumber(Integer semesterNumber) {
        this.semesterNumber = semesterNumber;
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
}
