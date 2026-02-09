package com.edufine.backend.service;

import com.edufine.backend.dto.CourseRequestDto;
import com.edufine.backend.dto.CourseResponseDto;
import com.edufine.backend.dto.LessonResponseDto;
import com.edufine.backend.dto.ModuleResponseDto;
import com.edufine.backend.dto.ModuleWithLessonsDto;
import com.edufine.backend.dto.SemesterModulesDto;
import com.edufine.backend.entity.Course;
import com.edufine.backend.entity.Module;
import com.edufine.backend.mapper.LessonMapper;
import com.edufine.backend.mapper.ModuleMapper;
import com.edufine.backend.repository.CourseRepository;
import com.edufine.backend.repository.LessonRepository;
import com.edufine.backend.repository.ModuleRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Map;
import java.util.LinkedHashMap;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Service
@Transactional
public class CourseService {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ModuleRepository moduleRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private ModuleMapper moduleMapper;

    @Autowired
    private LessonMapper lessonMapper;

    public List<CourseResponseDto> getCourseTree() {
        return courseRepository.findAll().stream()
                .map(this::mapCourseWithChildren)
                .collect(Collectors.toList());
    }

    public Optional<CourseResponseDto> getCourseTreeById(String id) {
        ObjectId objectId = new ObjectId(id);
        return courseRepository.findById(objectId)
                .map(this::mapCourseWithChildren);
    }

    private CourseResponseDto mapCourseWithChildren(Course course) {
        CourseResponseDto dto = new CourseResponseDto();
        dto.setId(course.getId() != null ? course.getId().toHexString() : null);
        dto.setCode(course.getCode());
        dto.setName(course.getName());
        dto.setDurationInYears(course.getDurationInYears());
        dto.setDescription(course.getDescription());
        dto.setTotalCredits(course.getTotalCredits());
        dto.setIsActive(course.getIsActive());
        dto.setCreatedAt(course.getCreatedAt());
        dto.setUpdatedAt(course.getUpdatedAt());

        List<ModuleWithLessonsDto> modules = moduleRepository.findByCourseId(course.getId().toHexString()).stream()
                .map(this::mapModuleWithLessons)
                .collect(Collectors.toList());
        dto.setModules(modules);
        
        List<String> moduleIds = modules.stream()
                .map(ModuleWithLessonsDto::getId)
                .collect(Collectors.toList());
        dto.setModuleIds(moduleIds);

        // Group modules by year and semester
        Map<Integer, Map<Integer, List<ModuleWithLessonsDto>>> byYearSemester = new LinkedHashMap<>();
        for (ModuleWithLessonsDto mod : modules) {
            Integer y = mod.getYear() != null ? mod.getYear() : 1;
            Integer sem = mod.getSemester() != null ? mod.getSemester() : 1;
            byYearSemester.computeIfAbsent(y, k -> new LinkedHashMap<>())
                    .computeIfAbsent(sem, k -> new java.util.ArrayList<>())
                    .add(mod);
        }
        
        List<SemesterModulesDto> semesters = new java.util.ArrayList<>();
        for (Map.Entry<Integer, Map<Integer, List<ModuleWithLessonsDto>>> yearEntry : byYearSemester.entrySet()) {
            for (Map.Entry<Integer, List<ModuleWithLessonsDto>> semEntry : yearEntry.getValue().entrySet()) {
                Integer semesterKey = yearEntry.getKey() * 10 + semEntry.getKey(); // e.g., Y1S2 = 12
                List<String> semModuleIds = semEntry.getValue().stream()
                        .map(ModuleWithLessonsDto::getId)
                        .collect(Collectors.toList());
                semesters.add(new SemesterModulesDto(semesterKey, semModuleIds, semEntry.getValue()));
            }
        }
        dto.setSemesters(semesters);
        
        return dto;
    }

    private ModuleWithLessonsDto mapModuleWithLessons(Module module) {
        ModuleResponseDto base = moduleMapper.toResponseDto(module);
        ModuleWithLessonsDto dto = new ModuleWithLessonsDto();
        dto.setId(base.getId());
        dto.setCourseId(base.getCourseId());
        dto.setCode(base.getCode());
        dto.setName(base.getName());
        dto.setSemester(base.getSemester());
        dto.setCredits(base.getCredits());
        dto.setLecturerId(base.getLecturerId());
        dto.setIsActive(base.getIsActive());
        dto.setCreatedAt(base.getCreatedAt());
        dto.setUpdatedAt(base.getUpdatedAt());

        List<LessonResponseDto> lessons = lessonRepository.findByModuleId(module.getId().toHexString()).stream()
                .map(lessonMapper::toResponseDto)
                .collect(Collectors.toList());
        dto.setLessons(lessons);
        return dto;
    }

    public List<CourseResponseDto> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapCourseWithChildren)
                .collect(Collectors.toList());
    }

    public CourseResponseDto createCourse(CourseRequestDto requestDto) {
        if (courseRepository.findByCode(requestDto.getCode()).isPresent()) {
            throw new RuntimeException("Course code already exists");
        }

        Course course = new Course();
        course.setCode(requestDto.getCode());
        course.setName(requestDto.getName());
        course.setDurationInYears(requestDto.getDurationInYears());
        course.setDescription(requestDto.getDescription());
        course.setTotalCredits(requestDto.getTotalCredits());
        course.setIsActive(requestDto.getIsActive());
        course.setModuleIds(new ArrayList<>());
        course.setCreatedAt(LocalDateTime.now());
        course.setUpdatedAt(LocalDateTime.now());

        Course saved = courseRepository.save(course);
        return mapCourseWithChildren(saved);
    }

    public CourseResponseDto updateCourse(String id, CourseRequestDto requestDto) {
        ObjectId objectId = new ObjectId(id);
        Course course = courseRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        course.setCode(requestDto.getCode());
        course.setName(requestDto.getName());
        course.setDurationInYears(requestDto.getDurationInYears());
        course.setDescription(requestDto.getDescription());
        course.setTotalCredits(requestDto.getTotalCredits());
        course.setIsActive(requestDto.getIsActive());
        course.setUpdatedAt(LocalDateTime.now());

        Course saved = courseRepository.save(course);
        return mapCourseWithChildren(saved);
    }

    public void deleteCourse(String id) {
        ObjectId objectId = new ObjectId(id);
        Course course = courseRepository.findById(objectId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        courseRepository.delete(course);
    }
}
