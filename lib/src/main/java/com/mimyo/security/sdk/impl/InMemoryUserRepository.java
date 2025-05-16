package com.mimyo.security.sdk.impl;

import com.mimyo.security.sdk.model.Role;
import com.mimyo.security.sdk.model.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class InMemoryUserRepository {
    private final Map<String, User> usersByUsername = new ConcurrentHashMap<>();
    private final Map<String, User> usersById = new ConcurrentHashMap<>();
    private final BCryptPasswordEncoder passwordEncoder;

    public InMemoryUserRepository() {
        this.passwordEncoder = new BCryptPasswordEncoder();
        // 기본 사용자 추가
        addDefaultUsers();
    }

    private void addDefaultUsers() {
        // 관리자 사용자 추가
        createUser("admin", "admin123", "Admin User", "admin@example.com", Set.of(Role.ADMIN));

        // 일반 사용자 추가
        createUser("user", "user123", "Regular User", "user@example.com", Set.of(Role.USER));
    }

    public User createUser(String username, String password, String name, String email, Set<Role> roles) {
        String userId = UUID.randomUUID().toString();
        String encodedPassword = passwordEncoder.encode(password);

        User user = new User(userId, username, encodedPassword, name, email, roles, true);
        usersByUsername.put(username, user);
        usersById.put(userId, user);

        return user;
    }

    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(usersByUsername.get(username));
    }

    public Optional<User> findById(String id) {
        return Optional.ofNullable(usersById.get(id));
    }

    public boolean existsByUsername(String username) {
        return usersByUsername.containsKey(username);
    }

    public void updateUser(User user) {
        if (user.getId() != null && usersById.containsKey(user.getId())) {
            usersByUsername.put(user.getUsername(), user);
            usersById.put(user.getId(), user);
        }
    }

    public void deleteUser(String id) {
        User user = usersById.remove(id);
        if (user != null) {
            usersByUsername.remove(user.getUsername());
        }
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}