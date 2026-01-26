package com.edufine.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class DepartmentRequestDto {

    @NotBlank(message = "Department name is required")
    @Size(min = 2, max = 100, message = "Department name must be between 2 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description must not exceed 500 characters")
    private String description;

    private String headId;

    // Constructors
    public DepartmentRequestDto() {}

    public DepartmentRequestDto(String name, String description, String headId) {
        this.name = name;
        this.description = description;
        this.headId = headId;
    }

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHeadId() { return headId; }
    public void setHeadId(String headId) { this.headId = headId; }
}
