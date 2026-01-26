package com.edufine.backend.controller.auth;

import com.edufine.backend.dto.LoginRequest;
import com.edufine.backend.dto.LoginResponse;
import com.edufine.backend.dto.StudentRequestDto;
import com.edufine.backend.dto.StudentResponseDto;
import com.edufine.backend.entity.User;
import com.edufine.backend.service.AuthService;
import com.edufine.backend.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private StudentService studentService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticateUser(loginRequest);
        return ResponseEntity.ok(loginResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@Valid @RequestBody User user) {
        User registeredUser = authService.registerUser(user);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/register/student")
    public ResponseEntity<StudentResponseDto> registerStudent(@Valid @RequestBody StudentRequestDto requestDto) {
        try {
            StudentResponseDto createdStudent = studentService.registerStudent(requestDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStudent);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
