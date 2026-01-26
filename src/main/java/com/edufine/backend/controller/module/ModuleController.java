package com.edufine.backend.controller.module;

import com.edufine.backend.dto.ModuleRequestDto;
import com.edufine.backend.dto.ModuleResponseDto;
import com.edufine.backend.dto.ModuleWithCourseDto;
import com.edufine.backend.dto.ModuleCompleteDto;
import com.edufine.backend.service.ModuleService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/modules")
public class ModuleController {

    @Autowired
    private ModuleService moduleService;

    @PostMapping
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<ModuleResponseDto> createModule(@Valid @RequestBody ModuleRequestDto requestDto) {
        ModuleResponseDto createdModule = moduleService.createModule(requestDto);
        return ResponseEntity.ok(createdModule);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<ModuleResponseDto>> getAllModules() {
        List<ModuleResponseDto> modules = moduleService.getAllModules();
        return ResponseEntity.ok(modules);
    }

    @GetMapping("/with-course")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<ModuleWithCourseDto>> getAllModulesWithCourse() {
        List<ModuleWithCourseDto> modules = moduleService.getAllModulesWithCourse();
        return ResponseEntity.ok(modules);
    }

    @GetMapping("/complete")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<ModuleCompleteDto>> getAllModulesComplete() {
        List<ModuleCompleteDto> modules = moduleService.getAllModulesComplete();
        return ResponseEntity.ok(modules);
    }

    @GetMapping("/course/{courseId}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<List<ModuleResponseDto>> getModulesByCourseId(@PathVariable String courseId) {
        List<ModuleResponseDto> modules = moduleService.getModulesByCourseId(courseId);
        return ResponseEntity.ok(modules);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<ModuleResponseDto> getModuleById(@PathVariable String id) {
        return moduleService.getModuleById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/code/{code}")
    @PreAuthorize("hasAnyRole('INSTRUCTOR', 'LECTURER', 'LIC', 'SUPER_ADMIN')")
    public ResponseEntity<ModuleResponseDto> getModuleByCode(@PathVariable String code) {
        return moduleService.getModuleByCode(code)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<ModuleResponseDto> updateModule(@PathVariable String id,
                                                         @Valid @RequestBody ModuleRequestDto requestDto) {
        ModuleResponseDto updatedModule = moduleService.updateModule(id, requestDto);
        return ResponseEntity.ok(updatedModule);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deleteModule(@PathVariable String id) {
        moduleService.deleteModule(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/deactivate")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> deactivateModule(@PathVariable String id) {
        moduleService.deactivateModule(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{id}/activate")
    @PreAuthorize("hasAnyRole('LIC', 'SUPER_ADMIN')")
    public ResponseEntity<Void> activateModule(@PathVariable String id) {
        moduleService.activateModule(id);
        return ResponseEntity.ok().build();
    }
}
