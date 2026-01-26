package com.edufine.backend.entity;

import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Document(collection = "students")
public class Student {

    @Id
    private ObjectId id;

    @Field
    private String userId; // Reference to User with STUDENT role

    @Field
    private String studentId; // Unique student identifier

    @Field
    private String username; // Username for login

    @Field
    private String password; // Encrypted password

    @Field
    private String firstName;

    @Field
    private String lastName;

    @Field
    private LocalDate dateOfBirth;

    @Field
    private String address;

    @Field
    private String phoneNumber;

    @Field
    private String emergencyContact;

    @Field
    private String enrollmentStatus; // ACTIVE, INACTIVE, GRADUATED, SUSPENDED

    @Field
    private LocalDate enrollmentDate;

    @Field
    private String currentGrade; // e.g., "10th Grade"

    @Field
    private Double gpa;

    @Field
    private boolean active = true;

    @Field
    private LocalDateTime createdAt;

    @Field
    private LocalDateTime updatedAt;

    // Constructors
    public Student() {}

    public Student(String userId, String studentId, String password, String firstName, String lastName, LocalDate dateOfBirth, String address,
                   String phoneNumber, String emergencyContact, String enrollmentStatus,
                   LocalDate enrollmentDate, String currentGrade) {
        this.userId = userId;
        this.studentId = studentId;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.emergencyContact = emergencyContact;
        this.enrollmentStatus = enrollmentStatus;
        this.enrollmentDate = enrollmentDate;
        this.currentGrade = currentGrade;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    // Getters and Setters
    public ObjectId getId() { return id; }
    public void setId(ObjectId id) { this.id = id; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmergencyContact() { return emergencyContact; }
    public void setEmergencyContact(String emergencyContact) { this.emergencyContact = emergencyContact; }

    public String getEnrollmentStatus() { return enrollmentStatus; }
    public void setEnrollmentStatus(String enrollmentStatus) { this.enrollmentStatus = enrollmentStatus; }

    public LocalDate getEnrollmentDate() { return enrollmentDate; }
    public void setEnrollmentDate(LocalDate enrollmentDate) { this.enrollmentDate = enrollmentDate; }

    public String getCurrentGrade() { return currentGrade; }
    public void setCurrentGrade(String currentGrade) { this.currentGrade = currentGrade; }

    public Double getGpa() { return gpa; }
    public void setGpa(Double gpa) { this.gpa = gpa; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
}
