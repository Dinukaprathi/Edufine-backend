package com.edufine.backend.config;

import com.edufine.backend.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(authz -> authz
                // Public endpoints - authentication/registration
                .requestMatchers("/api/auth/login", "/api/auth/register", "/api/auth/register/student").permitAll()
                // Removed course/module browsing as those features were deleted
                // Public endpoints - courses tree
                .requestMatchers(HttpMethod.GET, "/api/courses/**").permitAll()
                // Public endpoints - department browsing
                .requestMatchers(HttpMethod.GET, "/api/departments/**").permitAll()
                // Public endpoints - notices browsing (GET requests only)
                .requestMatchers(HttpMethod.GET, "/api/notices/**").permitAll()
                // All other requests require authentication (role checks done via @PreAuthorize)
                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Role hierarchy: SUPER_ADMIN inherits all roles below it
     * SUPER_ADMIN > LIC > LECTURER > STAFF > INSTRUCTOR > STUDENT
     */
    @Bean
    public RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        String hierarchy = "ROLE_SUPER_ADMIN > ROLE_LIC \n" +
                          "ROLE_LIC > ROLE_LECTURER \n" +
                          "ROLE_LECTURER > ROLE_INSTRUCTOR \n" +
                          "ROLE_INSTRUCTOR > ROLE_STAFF \n" +
                          "ROLE_STAFF > ROLE_STUDENT";
        roleHierarchy.setHierarchy(hierarchy);
        return roleHierarchy;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
