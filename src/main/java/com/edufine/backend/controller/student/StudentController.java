package com.edufine.backend.controller.student;

import com.edufine.backend.dto.StudentRequestDto;
import com.edufine.backend.dto.StudentResponseDto;
import com.edufine.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    @Autowired
    private StudentService studentService;

    @GetMapping
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<StudentResponseDto>> getAllStudents() {
        List<StudentResponseDto> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/active")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<StudentResponseDto>> getActiveStudents() {
        List<StudentResponseDto> students = studentService.getActiveStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<StudentResponseDto> getStudentById(@PathVariable String id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userId}")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<StudentResponseDto> getStudentByUserId(@PathVariable String userId) {
        return studentService.getStudentByUserId(userId)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/enrollment-status/{status}")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<StudentResponseDto>> getStudentsByEnrollmentStatus(@PathVariable String status) {
        List<StudentResponseDto> students = studentService.getStudentsByEnrollmentStatus(status);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/grade/{grade}")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<StudentResponseDto>> getStudentsByGrade(@PathVariable String grade) {
        List<StudentResponseDto> students = studentService.getStudentsByGrade(grade);
        return ResponseEntity.ok(students);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<StudentResponseDto> updateStudent(@PathVariable String id,
                                                           @Valid @RequestBody StudentRequestDto requestDto) {
        try {
            StudentResponseDto updatedStudent = studentService.updateStudent(id, requestDto);
            return ResponseEntity.ok(updatedStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteStudent(@PathVariable String id) {
        try {
            studentService.deleteStudent(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deactivateStudent(@PathVariable String id) {
        try {
            studentService.deactivateStudent(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('STAFF', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> activateStudent(@PathVariable String id) {
        try {
            studentService.activateStudent(id);
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
