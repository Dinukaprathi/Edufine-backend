package com.edufine.backend.service;

import com.edufine.backend.dto.ModuleRequestDto;
import com.edufine.backend.dto.ModuleResponseDto;
import com.edufine.backend.dto.ModuleWithCourseDto;
import com.edufine.backend.dto.ModuleCompleteDto;
import com.edufine.backend.dto.LessonWithMaterialsDto;
import com.edufine.backend.dto.MaterialDto;
import com.edufine.backend.entity.Module;
import com.edufine.backend.mapper.ModuleMapper;
import com.edufine.backend.mapper.CourseMapper;
import com.edufine.backend.mapper.MaterialMapper;
import com.edufine.backend.repository.ModuleRepository;
import com.edufine.backend.repository.CourseRepository;
import com.edufine.backend.repository.LessonRepository;
import com.edufine.backend.repository.MaterialRepository;
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
public class ModuleService {

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private MaterialRepository materialRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private CourseMapper courseMapper;

    @Autowired
    private MaterialMapper materialMapper;

    public ModuleResponseDto createModule(ModuleRequestDto requestDto) {
        Optional<Module> existingModule = moduleRepository.findByCode(requestDto.getCode());
        if (existingModule.isPresent()) {
            throw new RuntimeException("Module code already exists");
        }
        Module module = moduleMapper.toEntity(requestDto);
        module.setIsActive(true);
        module.setCreatedAt(LocalDateTime.now());
        module.setUpdatedAt(LocalDateTime.now());
        Module savedModule = moduleRepository.save(module);
        return moduleMapper.toResponseDto(savedModule);
    }

    public List<ModuleResponseDto> getAllModules() {
        return moduleRepository.findAll().stream()
                .map(moduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public List<ModuleWithCourseDto> getAllModulesWithCourse() {
        return moduleRepository.findAll().stream()
                .map(this::mapModuleWithCourse)
                .collect(Collectors.toList());
    }

    public List<ModuleCompleteDto> getAllModulesComplete() {
        return moduleRepository.findAll().stream()
                .map(this::mapModuleComplete)
                .collect(Collectors.toList());
    }

    public List<ModuleResponseDto> getModulesByCourseId(String courseId) {
        return moduleRepository.findByCourseId(courseId).stream()
                .map(moduleMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    public Optional<ModuleResponseDto> getModuleById(String id) {
        ObjectId objectId = new ObjectId(id);
        return moduleRepository.findById(objectId)
                .map(moduleMapper::toResponseDto);
    }

    public Optional<ModuleResponseDto> getModuleByCode(String code) {
        return moduleRepository.findByCode(code)
                .map(moduleMapper::toResponseDto);
    }

    public ModuleResponseDto updateModule(String id, ModuleRequestDto requestDto) {
        ObjectId objectId = new ObjectId(id);
        Module module = moduleRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Module not found"));

        if (!module.getCode().equals(requestDto.getCode())) {
            Optional<Module> existingModule = moduleRepository.findByCode(requestDto.getCode());
            if (existingModule.isPresent()) {
                throw new RuntimeException("Module code already exists");
            }
        }

        moduleMapper.updateEntityFromDto(requestDto, module);
        module.setUpdatedAt(LocalDateTime.now());
        Module updatedModule = moduleRepository.save(module);
        return moduleMapper.toResponseDto(updatedModule);
    }

    public void deleteModule(String id) {
        ObjectId objectId = new ObjectId(id);
        Module module = moduleRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        moduleRepository.delete(module);
    }

    public void deactivateModule(String id) {
        ObjectId objectId = new ObjectId(id);
        Module module = moduleRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        module.setIsActive(false);
        module.setUpdatedAt(LocalDateTime.now());
        moduleRepository.save(module);
    }

    public void activateModule(String id) {
        ObjectId objectId = new ObjectId(id);
        Module module = moduleRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Module not found"));
        module.setIsActive(true);
        module.setUpdatedAt(LocalDateTime.now());
        moduleRepository.save(module);
    }

    private ModuleWithCourseDto mapModuleWithCourse(Module module) {
        ModuleWithCourseDto dto = new ModuleWithCourseDto();
        dto.setId(module.getId().toHexString());
        dto.setCode(module.getCode());
        dto.setName(module.getName());
        dto.setYear(module.getYear());
        dto.setSemester(module.getSemester());
        dto.setCredits(module.getCredits());
        dto.setLecturerId(module.getLecturerId());
        dto.setIsActive(module.getIsActive());
        dto.setCreatedAt(module.getCreatedAt());
        dto.setUpdatedAt(module.getUpdatedAt());

        // Fetch and map course
        if (module.getCourseId() != null) {
            try {
                ObjectId courseId = new ObjectId(module.getCourseId());
                courseRepository.findById(courseId).ifPresent(course -> {
                    dto.setCourse(courseMapper.toResponseDto(course));
                });
            } catch (IllegalArgumentException e) {
                // Invalid course ID format
            }
        }
        return dto;
    }

    private ModuleCompleteDto mapModuleComplete(Module module) {
        ModuleCompleteDto dto = new ModuleCompleteDto();
        dto.setId(module.getId().toHexString());
        dto.setCode(module.getCode());
        dto.setName(module.getName());
        dto.setYear(module.getYear());
        dto.setSemester(module.getSemester());
        dto.setCredits(module.getCredits());
        dto.setLecturerId(module.getLecturerId());
        dto.setIsActive(module.getIsActive());
        dto.setCreatedAt(module.getCreatedAt());
        dto.setUpdatedAt(module.getUpdatedAt());

        // Fetch and map course
        if (module.getCourseId() != null) {
            try {
                ObjectId courseId = new ObjectId(module.getCourseId());
                courseRepository.findById(courseId).ifPresent(course -> {
                    dto.setCourse(courseMapper.toResponseDto(course));
                });
            } catch (IllegalArgumentException e) {
                // Invalid course ID format
            }
        }

        // Fetch and map lessons with materials
        List<LessonWithMaterialsDto> lessons = lessonRepository.findByModuleId(module.getId().toHexString()).stream()
                .map(lesson -> {
                    LessonWithMaterialsDto lessonDto = new LessonWithMaterialsDto();
                    lessonDto.setId(lesson.getId().toHexString());
                    lessonDto.setModuleId(lesson.getModuleId());
                    lessonDto.setTitle(lesson.getTitle());
                    lessonDto.setDescription(lesson.getDescription());
                    lessonDto.setLessonOrder(lesson.getLessonOrder());
                    lessonDto.setScheduledAt(lesson.getScheduledAt());
                    lessonDto.setCreatedBy(lesson.getCreatedBy());
                    lessonDto.setCreatedAt(lesson.getCreatedAt());
                    lessonDto.setUpdatedAt(lesson.getUpdatedAt());

                    // Fetch materials for this lesson
                    List<MaterialDto> materials = materialRepository.findByLessonId(lesson.getId().toHexString()).stream()
                            .map(materialMapper::toResponseDto)
                            .collect(Collectors.toList());
                    lessonDto.setMaterials(materials);
                    return lessonDto;
                })
                .collect(Collectors.toList());
        dto.setLessons(lessons);

        return dto;
    }
}
