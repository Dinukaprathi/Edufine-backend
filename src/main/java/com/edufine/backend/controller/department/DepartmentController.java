package com.edufine.backend.controller.department;

import com.edufine.backend.dto.DepartmentRequestDto;
import com.edufine.backend.dto.DepartmentResponseDto;
import com.edufine.backend.service.DepartmentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/departments")
public class DepartmentController {

    @Autowired
    private DepartmentService departmentService;

    @PostMapping
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<DepartmentResponseDto> createDepartment(@Valid @RequestBody DepartmentRequestDto requestDto) {
        DepartmentResponseDto createdDepartment = departmentService.createDepartment(requestDto);
        return ResponseEntity.ok(createdDepartment);
    }

    @GetMapping
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<DepartmentResponseDto>> getAllDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getAllDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/active")
    @PreAuthorize("permitAll()")
    public ResponseEntity<List<DepartmentResponseDto>> getActiveDepartments() {
        List<DepartmentResponseDto> departments = departmentService.getActiveDepartments();
        return ResponseEntity.ok(departments);
    }

    @GetMapping("/{id}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<DepartmentResponseDto> getDepartmentById(@PathVariable String id) {
        return departmentService.getDepartmentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    @PreAuthorize("permitAll()")
    public ResponseEntity<DepartmentResponseDto> getDepartmentByName(@PathVariable String name) {
        return departmentService.getDepartmentByName(name)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<DepartmentResponseDto> updateDepartment(@PathVariable String id,
                                                                 @Valid @RequestBody DepartmentRequestDto requestDto) {
        DepartmentResponseDto updatedDepartment = departmentService.updateDepartment(id, requestDto);
        return ResponseEntity.ok(updatedDepartment);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteDepartment(@PathVariable String id) {
        departmentService.deleteDepartment(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deactivateDepartment(@PathVariable String id) {
        departmentService.deactivateDepartment(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> activateDepartment(@PathVariable String id) {
        departmentService.activateDepartment(id);
        return ResponseEntity.ok().build();
    }
}
