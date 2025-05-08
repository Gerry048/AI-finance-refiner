package com.financetracker.model;

import java.util.Objects;
import java.util.UUID;

public class User {
    // 注意：将 userId 重命名为 id 以保持一致性，并保持 final
    private final String id;
    private String username;
    private String passwordHash;
    private String email;

    // --- 新增构造函数 (用于从存储加载) ---
    public User(String id, String username, String passwordHash, String email) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(username, "Username cannot be null");
        Objects.requireNonNull(passwordHash, "Password hash cannot be null");
        // email 可以为 null
        this.id = id;
        this.username = username;
        this.passwordHash = passwordHash;
        this.email = email;
    }

    // --- 修改原有的公共构造函数 ---
    // 用于创建全新的 User 对象
    public User(String username, String passwordHash, String email) {
        this(UUID.randomUUID().toString(), username, passwordHash, email);
    }

    // Getters (修改 getUserId 为 getId)
    public String getId() { return id; } // 重命名
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getEmail() { return email; }

    // Setters (保持不变)
    public void setUsername(String username) { this.username = username; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }
    public void setEmail(String email) { this.email = email; }

    // equals(), hashCode(), toString() (保持不变, 基于 ID 或 username)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        // 可以优先使用 ID 判断
        return Objects.equals(id, user.id);
        // 或者保持原来的逻辑: return Objects.equals(username, user.username);
    }

    @Override
    public int hashCode() {
        // 对应 equals 的修改
        return Objects.hash(id);
        // 或者保持原来的逻辑: return Objects.hash(username);
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' + // 修改 userId 为 id
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}