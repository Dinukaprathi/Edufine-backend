package com.edufine.backend.service;

import com.edufine.backend.dto.DepartmentRequestDto;
import com.edufine.backend.dto.DepartmentResponseDto;
import com.edufine.backend.entity.Department;
import com.edufine.backend.mapper.DepartmentMapper;
import com.edufine.backend.repository.DepartmentRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class DepartmentService {

    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentMapper departmentMapper;

    public DepartmentResponseDto createDepartment(DepartmentRequestDto requestDto) {
        if (departmentRepository.existsByName(requestDto.getName())) {
            throw new RuntimeException("Department name already exists");
        }
        Department department = departmentMapper.toEntity(requestDto);
        department.setActive(true);
        department.setCreatedAt(LocalDateTime.now());
        department.setUpdatedAt(LocalDateTime.now());
        Department savedDepartment = departmentRepository.save(department);
        return departmentMapper.toResponseDto(savedDepartment);
    }

    public List<DepartmentResponseDto> getAllDepartments() {
        return departmentRepository.findAll().stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<DepartmentResponseDto> getActiveDepartments() {
        return departmentRepository.findByActive(true).stream()
                .map(departmentMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<DepartmentResponseDto> getDepartmentById(String id) {
        ObjectId objectId = new ObjectId(id);
        return departmentRepository.findById(objectId)
                .map(departmentMapper::toResponseDto);
    }

    public Optional<DepartmentResponseDto> getDepartmentByName(String name) {
        return departmentRepository.findByName(name)
                .map(departmentMapper::toResponseDto);
    }

    public DepartmentResponseDto updateDepartment(String id, DepartmentRequestDto requestDto) {
        ObjectId objectId = new ObjectId(id);
        Department department = departmentRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        if (!department.getName().equals(requestDto.getName()) &&
            departmentRepository.existsByName(requestDto.getName())) {
            throw new RuntimeException("Department name already exists");
        }

        departmentMapper.updateEntityFromDto(requestDto, department);
        department.setUpdatedAt(LocalDateTime.now());
        Department updatedDepartment = departmentRepository.save(department);
        return departmentMapper.toResponseDto(updatedDepartment);
    }

    public void deleteDepartment(String id) {
        ObjectId objectId = new ObjectId(id);
        Department department = departmentRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        departmentRepository.delete(department);
    }

    public void deactivateDepartment(String id) {
        ObjectId objectId = new ObjectId(id);
        Department department = departmentRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        department.setActive(false);
        department.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(department);
    }

    public void activateDepartment(String id) {
        ObjectId objectId = new ObjectId(id);
        Department department = departmentRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Department not found"));
        department.setActive(true);
        department.setUpdatedAt(LocalDateTime.now());
        departmentRepository.save(department);
    }
}
