package com.edufine.backend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDateTime;

@Document(collection = "departments")
public class Department {

    @Id
    private ObjectId id;

    @Field
    private String name;

    @Field
    private String description;

    @Field
    private String headId; // Reference to Teacher's user ID

    @Field
    private boolean active = true;

    @Field
    private LocalDateTime createdAt;

    @Field
    private LocalDateTime updatedAt;

    // Constructors
    public Department() {}

    public Department(String name, String description, String headId) {
        this.name = name;
        this.description = description;
        this.headId = headId;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getHeadId() { return headId; }
    public void setHeadId(String headId) { this.headId = headId; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
