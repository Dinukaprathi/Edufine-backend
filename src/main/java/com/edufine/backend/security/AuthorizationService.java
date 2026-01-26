package com.edufine.backend.security;

import com.edufine.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

/**
 * Custom authorization service for complex permission checks
 * Used in SpEL expressions with @PreAuthorize annotations
 */
@Component("authorizationService") // renamed bean to avoid conflict with AuthService
public class AuthorizationService {

    @Autowired
    private UserRepository userRepository;

    /**
     * Check if current authenticated user has SUPER_ADMIN role
     * SUPER_ADMIN bypasses many checks
     */
    public boolean isSuperAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }

    /**
     * Check if user has any of the student management roles
     * STAFF, LECTURER, LIC, or SUPER_ADMIN
     */
    public boolean canManageStudents() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_STAFF") ||
                               ga.getAuthority().equals("ROLE_LECTURER") ||
                               ga.getAuthority().equals("ROLE_LIC") ||
                               ga.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }

    /**
     * Check if user can manage system users/staff (LIC or SUPER_ADMIN)
     */
    public boolean canManageUsers() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return false;
        }
        return auth.getAuthorities().stream()
                .anyMatch(ga -> ga.getAuthority().equals("ROLE_LIC") ||
                               ga.getAuthority().equals("ROLE_SUPER_ADMIN"));
    }
}
