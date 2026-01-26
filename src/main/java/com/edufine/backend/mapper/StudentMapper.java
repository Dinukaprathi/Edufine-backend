package com.edufine.backend.mapper;

import com.edufine.backend.dto.StudentResponseDto;
import com.edufine.backend.entity.Student;
import com.edufine.backend.entity.User;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {

    public StudentResponseDto toResponseDto(Student student, User user) {
        StudentResponseDto dto = new StudentResponseDto();
        dto.setId(student.getId() != null ? student.getId().toHexString() : null);
        dto.setUserId(student.getUserId());
        dto.setStudentId(student.getStudentId());
        dto.setDateOfBirth(student.getDateOfBirth());
        dto.setAddress(student.getAddress());
        dto.setPhoneNumber(student.getPhoneNumber());
        dto.setEmergencyContact(student.getEmergencyContact());
        dto.setEnrollmentStatus(student.getEnrollmentStatus());
        dto.setEnrollmentDate(student.getEnrollmentDate());
        dto.setCurrentGrade(student.getCurrentGrade());
        dto.setGpa(student.getGpa());
        dto.setActive(student.isActive());
        dto.setCreatedAt(student.getCreatedAt());
        dto.setUpdatedAt(student.getUpdatedAt());
        
        if (user != null) {
            dto.setUsername(user.getUsername());
            dto.setEmail(user.getEmail());
            dto.setFirstName(user.getFirstName());
            dto.setLastName(user.getLastName());
        }
        
        return dto;
    }
}
