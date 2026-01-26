package com.edufine.backend.dto;

import java.util.HashMap;
import java.util.Map;

public class LoginResponse {

    private String token;
    private String type = "Bearer";
    private String username;
    private String role;
    private Map<String, Boolean> permissions;

    // Constructors
    public LoginResponse() {
        this.permissions = new HashMap<>();
    }

    public LoginResponse(String token, String username, String role) {
        this.token = token;
        this.username = username;
        this.role = role;
        this.permissions = new HashMap<>();
    }

    // Getters and Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public Map<String, Boolean> getPermissions() { return permissions; }
    public void setPermissions(Map<String, Boolean> permissions) { this.permissions = permissions; }
}
