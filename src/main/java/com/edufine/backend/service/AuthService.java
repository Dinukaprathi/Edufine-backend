package com.edufine.backend.service;

import com.edufine.backend.dto.LoginRequest;
import com.edufine.backend.dto.LoginResponse;
import com.edufine.backend.entity.User;
import com.edufine.backend.entity.UserRole;
import com.edufine.backend.repository.UserRepository;
import com.edufine.backend.repository.StudentRepository;
import com.edufine.backend.repository.StaffRepository;
import com.edufine.backend.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public LoginResponse authenticateUser(LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Validate based on role
        if (user.getRole() == UserRole.STUDENT) {
            // Validate student exists in student collection
            if (!studentRepository.existsByUserId(user.getId().toHexString())) {
                throw new RuntimeException("Student record not found");
            }
        } else if (user.getRole() == UserRole.STAFF ||
                   user.getRole() == UserRole.INSTRUCTOR ||
                   user.getRole() == UserRole.LECTURER ||
                   user.getRole() == UserRole.LIC ||
                   user.getRole() == UserRole.SUPER_ADMIN) {
            // Validate staff exists in staff collection
            if (!staffRepository.existsByUserId(user.getId().toHexString())) {
                throw new RuntimeException("Staff record not found");
            }
        } else {
            throw new RuntimeException("User role is not authorized for authentication");
        }

        String jwt = jwtUtil.generateToken(userDetails);

        LoginResponse response = new LoginResponse(jwt, user.getUsername(), user.getRole().name());
        response.setPermissions(getPermissionsForRole(user.getRole()));

        return response;
    }

    /**
     * Get permissions map based on user role
     */
    private Map<String, Boolean> getPermissionsForRole(UserRole role) {
        Map<String, Boolean> permissions = new HashMap<>();

        // Students have no administrative permissions
        if (role == UserRole.STUDENT) {
            permissions.put("canManageStudents", false);
            permissions.put("canManageCourses", false);
            permissions.put("canManageNotices", false);
            permissions.put("canManageUsers", false);
            permissions.put("canManageDepartments", false);
            permissions.put("canManageModules", false);
            return permissions;
        }

        // STAFF: Can manage students and notices only
        if (role == UserRole.STAFF) {
            permissions.put("canManageStudents", true);
            permissions.put("canManageCourses", false); // courses removed
            permissions.put("canManageNotices", true);
            permissions.put("canManageUsers", false);
            permissions.put("canManageDepartments", false);
            permissions.put("canManageModules", false); // modules removed
            return permissions;
        }

        // INSTRUCTOR: Courses/modules removed; restrict to notices
        if (role == UserRole.INSTRUCTOR) {
            permissions.put("canManageStudents", false);
            permissions.put("canManageCourses", false);
            permissions.put("canManageNotices", true);
            permissions.put("canManageUsers", false);
            permissions.put("canManageDepartments", false);
            permissions.put("canManageModules", false);
            return permissions;
        }

        // LECTURER: Can manage students, notices, and add staff
        if (role == UserRole.LECTURER) {
            permissions.put("canManageStudents", true);
            permissions.put("canManageCourses", false);
            permissions.put("canManageNotices", true);
            permissions.put("canManageUsers", true);  // Can add staff
            permissions.put("canManageDepartments", false);
            permissions.put("canManageModules", false);
            return permissions;
        }

        // LIC: Can manage notices, users, departments
        if (role == UserRole.LIC) {
            permissions.put("canManageStudents", true);
            permissions.put("canManageCourses", false);
            permissions.put("canManageNotices", true);
            permissions.put("canManageUsers", true);
            permissions.put("canManageDepartments", true);
            permissions.put("canManageModules", false);
            return permissions;
        }

        // SUPER_ADMIN: Full access to remaining modules
        if (role == UserRole.SUPER_ADMIN) {
            permissions.put("canManageStudents", true);
            permissions.put("canManageCourses", false);
            permissions.put("canManageNotices", true);
            permissions.put("canManageUsers", true);
            permissions.put("canManageDepartments", true);
            permissions.put("canManageModules", false);
            return permissions;
        }

        return permissions;
    }

    public User registerUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        // Encode password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}

