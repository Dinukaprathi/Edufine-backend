package com.edufine.backend.controller.course;

import com.edufine.backend.dto.CourseRequestDto;
import com.edufine.backend.dto.CourseResponseDto;
import com.edufine.backend.service.CourseService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('STAFF') or hasRole('INSTRUCTOR')")
    public ResponseEntity<List<CourseResponseDto>> getAllCoursesForLecturerAndAbove() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @PostMapping
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<CourseResponseDto> createCourse(@Valid @RequestBody CourseRequestDto requestDto) {
        return ResponseEntity.ok(courseService.createCourse(requestDto));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<CourseResponseDto> updateCourse(@PathVariable String id,
                                                          @Valid @RequestBody CourseRequestDto requestDto) {
        return ResponseEntity.ok(courseService.updateCourse(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LECTURER')")
    public ResponseEntity<Void> deleteCourse(@PathVariable String id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }
}