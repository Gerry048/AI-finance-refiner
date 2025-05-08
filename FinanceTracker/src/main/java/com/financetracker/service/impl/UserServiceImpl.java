package com.financetracker.service.impl;

import com.financetracker.model.User;
import com.financetracker.repository.DataStorage;
import com.financetracker.service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UserServiceImpl implements UserService {
    private final DataStorage storage;
    private List<User> users;

    public UserServiceImpl(DataStorage storage) {
        this.storage = storage;
        try {
            this.users = new ArrayList<>(storage.loadUsers());
        } catch (IOException e) {
            this.users = new ArrayList<>();
            System.err.println("警告: 无法加载用户数据，使用空列表: " + e.getMessage());
        }
        // 如果 CSV 里一开始没有任何用户，可以在这里自动注入一个默认管理员：
        if (users.isEmpty()) {
            User admin = new User("admin", "admin", "admin@example.com");
            users.add(admin);
            try { storage.saveUsers(users); }
            catch (IOException ex) { System.err.println("无法写入默认管理员: " + ex.getMessage()); }
        }
    }

    @Override
    public User registerUser(String username, String password, String email) {
        // 简单校验
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("用户名不能为空");
        }
        if (users.stream().anyMatch(u -> u.getUsername().equals(username))) {
            throw new IllegalArgumentException("用户名已存在");
        }
        User u = new User(username, password, email);
        users.add(u);
        try {
            storage.saveUsers(users);
        } catch (IOException e) {
            users.remove(u);
            throw new RuntimeException("注册失败，无法保存用户数据: " + e.getMessage(), e);
        }
        return u;
    }

    @Override
    public User authenticateUser(String username, String password) {
        // 重新加载，保证能看到最新注册的账号
        try {
            users = new ArrayList<>(storage.loadUsers());
        } catch (IOException e) {
            System.err.println("加载用户失败，将使用缓存: " + e.getMessage());
        }
        return users.stream()
                .filter(u -> u.getUsername().equals(username)
                        && u.getPasswordHash().equals(password))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User findUserByUsername(String username) {
        return users.stream()
                .filter(u -> u.getUsername().equals(username))
                .findFirst()
                .orElse(null);
    }

    @Override
    public User updateUser(User user, String newPassword) {
        for (int i = 0; i < users.size(); i++) {
            if (users.get(i).getId().equals(user.getId())) {
                User u = users.get(i);
                u.setEmail(user.getEmail());
                if (newPassword != null && !newPassword.isBlank()) {
                    u.setPasswordHash(newPassword);
                }
                try {
                    storage.saveUsers(users);
                } catch (IOException e) {
                    throw new RuntimeException("更新用户失败: " + e.getMessage(), e);
                }
                return u;
            }
        }
        throw new IllegalArgumentException("未找到要更新的用户");
    }
}
