package com.edufine.backend.controller.course;

import com.edufine.backend.dto.CourseResponseDto;
import com.edufine.backend.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    @Autowired
    private CourseService courseService;

    @GetMapping("/tree")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<CourseResponseDto>> getCourseTree() {
        return ResponseEntity.ok(courseService.getCourseTree());
    }

    @GetMapping("/tree/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<CourseResponseDto> getCourseTreeById(@PathVariable String id) {
        return courseService.getCourseTreeById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    @PreAuthorize("hasRole('LECTURER') or hasRole('ADMIN')")
    public ResponseEntity<List<CourseResponseDto>> getAllCoursesForLecturerAndAbove() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }
}
