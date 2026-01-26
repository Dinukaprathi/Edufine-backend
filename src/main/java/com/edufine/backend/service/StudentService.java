package com.edufine.backend.service;

import com.edufine.backend.dto.StudentRequestDto;
import com.edufine.backend.dto.StudentResponseDto;
import com.edufine.backend.entity.Student;
import com.edufine.backend.entity.User;
import com.edufine.backend.entity.UserRole;
import com.edufine.backend.mapper.StudentMapper;
import com.edufine.backend.repository.StudentRepository;
import com.edufine.backend.repository.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentMapper studentMapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    public StudentResponseDto registerStudent(StudentRequestDto requestDto) {
        // Check if username already exists
        if (userRepository.existsByUsername(requestDto.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }

        // Check if email already exists
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        // Check if student ID already exists
        if (studentRepository.existsByStudentId(requestDto.getStudentId())) {
            throw new IllegalArgumentException("Student ID already exists");
        }

        // Create User entity
        User user = new User();
        user.setUsername(requestDto.getUsername());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setRole(UserRole.STUDENT);
        user.setActive(true);
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());

        User savedUser = userRepository.save(user);

        // Create Student entity
        Student student = new Student();
        student.setUserId(savedUser.getId().toHexString());
        student.setStudentId(requestDto.getStudentId());
        student.setPassword(passwordEncoder.encode(requestDto.getPassword()));
        student.setDateOfBirth(requestDto.getDateOfBirth());
        student.setAddress(requestDto.getAddress());
        student.setPhoneNumber(requestDto.getPhoneNumber());
        student.setEmergencyContact(requestDto.getEmergencyContact());
        student.setEnrollmentStatus(requestDto.getEnrollmentStatus());
        student.setEnrollmentDate(requestDto.getEnrollmentDate());
        student.setCurrentGrade(requestDto.getCurrentGrade());
        student.setActive(true);
        student.setCreatedAt(LocalDateTime.now());
        student.setUpdatedAt(LocalDateTime.now());

        Student savedStudent = studentRepository.save(student);

        return studentMapper.toResponseDto(savedStudent, savedUser);
    }

    public List<StudentResponseDto> getAllStudents() {
        List<Student> students = studentRepository.findAll();
        return students.stream()
                .map(student -> {
                    User user = userRepository.findById(new ObjectId(student.getUserId())).orElse(null);
                    return studentMapper.toResponseDto(student, user);
                })
                .collect(Collectors.toList());
    }

    public List<StudentResponseDto> getActiveStudents() {
        List<Student> students = studentRepository.findByActive(true);
        return students.stream()
                .map(student -> {
                    User user = userRepository.findById(new ObjectId(student.getUserId())).orElse(null);
                    return studentMapper.toResponseDto(student, user);
                })
                .collect(Collectors.toList());
    }

    public Optional<StudentResponseDto> getStudentById(String id) {
        return studentRepository.findById(new ObjectId(id))
                .map(student -> {
                    User user = userRepository.findById(new ObjectId(student.getUserId())).orElse(null);
                    return studentMapper.toResponseDto(student, user);
                });
    }

    public Optional<StudentResponseDto> getStudentByUserId(String userId) {
        return studentRepository.findByUserId(userId)
                .map(student -> {
                    User user = userRepository.findById(new ObjectId(userId)).orElse(null);
                    return studentMapper.toResponseDto(student, user);
                });
    }

    public List<StudentResponseDto> getStudentsByEnrollmentStatus(String status) {
        List<Student> students = studentRepository.findByEnrollmentStatus(status);
        return students.stream()
                .map(student -> {
                    User user = userRepository.findById(new ObjectId(student.getUserId())).orElse(null);
                    return studentMapper.toResponseDto(student, user);
                })
                .collect(Collectors.toList());
    }

    public List<StudentResponseDto> getStudentsByGrade(String grade) {
        List<Student> students = studentRepository.findByCurrentGrade(grade);
        return students.stream()
                .map(student -> {
                    User user = userRepository.findById(new ObjectId(student.getUserId())).orElse(null);
                    return studentMapper.toResponseDto(student, user);
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentResponseDto updateStudent(String id, StudentRequestDto requestDto) {
        Student student = studentRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        User user = userRepository.findById(new ObjectId(student.getUserId()))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Update User details
        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);

        // Update Student details
        student.setStudentId(requestDto.getStudentId());
        student.setDateOfBirth(requestDto.getDateOfBirth());
        student.setAddress(requestDto.getAddress());
        student.setPhoneNumber(requestDto.getPhoneNumber());
        student.setEmergencyContact(requestDto.getEmergencyContact());
        student.setEnrollmentStatus(requestDto.getEnrollmentStatus());
        student.setEnrollmentDate(requestDto.getEnrollmentDate());
        student.setCurrentGrade(requestDto.getCurrentGrade());
        student.setUpdatedAt(LocalDateTime.now());

        Student updatedStudent = studentRepository.save(student);

        return studentMapper.toResponseDto(updatedStudent, user);
    }

    @Transactional
    public void deleteStudent(String id) {
        Student student = studentRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        // Delete associated user
        userRepository.deleteById(new ObjectId(student.getUserId()));

        // Delete student
        studentRepository.deleteById(new ObjectId(id));
    }

    @Transactional
    public void deactivateStudent(String id) {
        Student student = studentRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        student.setActive(false);
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.save(student);

        // Also deactivate user
        User user = userRepository.findById(new ObjectId(student.getUserId())).orElse(null);
        if (user != null) {
            user.setActive(false);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Transactional
    public void activateStudent(String id) {
        Student student = studentRepository.findById(new ObjectId(id))
                .orElseThrow(() -> new IllegalArgumentException("Student not found with id: " + id));

        student.setActive(true);
        student.setUpdatedAt(LocalDateTime.now());
        studentRepository.save(student);

        // Also activate user
        User user = userRepository.findById(new ObjectId(student.getUserId())).orElse(null);
        if (user != null) {
            user.setActive(true);
            user.setUpdatedAt(LocalDateTime.now());
            userRepository.save(user);
        }
    }
}
