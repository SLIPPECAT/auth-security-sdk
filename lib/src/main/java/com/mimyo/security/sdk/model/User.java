package com.mimyo.security.sdk.model;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Set;

public class User {
    private String id;
    private String username;
    private String password;  // 해시된 패스워드
    private String name;
    private String email;
    private Set<Role> roles;
    private boolean active;

    // 생성자
    public User() {}

    public User(String id, String username, String password, String name, String email, Set<Role> roles, boolean active) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.roles = roles;
        this.active = active;
    }

    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Role> getRoles() { return roles; }
    public void setRoles(Set<Role> roles) { this.roles = roles; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    // 편의 메서드
    public boolean passwordMatches(String rawPassword) {
        // 여기서는 BCryptPasswordEncoder 사용 (실제로는 Bean 주입 필요)
        return new BCryptPasswordEncoder().matches(rawPassword, this.password);
    }

    public List<String> getAuthorities() {
        return roles.stream()
                .flatMap(role -> role.getPermissions().stream())
                .map(Permission::getName)
                .distinct()
                .toList();
    }
}