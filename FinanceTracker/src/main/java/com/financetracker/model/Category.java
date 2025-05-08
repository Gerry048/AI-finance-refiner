package com.financetracker.model;

import java.util.Objects;
import java.util.UUID;

public class Category {
    private final String id; // 保持 final
    private String name;

    // --- 新增构造函数 (用于从存储加载) ---
    // 可以是 public 或包级私有(如果只有 repository 包需要访问)
    public Category(String id, String name) {
        Objects.requireNonNull(id, "ID cannot be null"); // 添加校验
        Objects.requireNonNull(name, "Name cannot be null");
        this.id = id;
        this.name = name;
    }

    // --- 修改原有的公共构造函数 ---
    // 用于创建全新的 Category 对象
    public Category(String name) {
        // 生成新的 UUID，然后调用上面的构造函数
        this(UUID.randomUUID().toString(), name);
    }

    // Getters and Setters (保持不变)
    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // equals(), hashCode(), toString() (保持不变, 基于 ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Category category = (Category) o;
        return Objects.equals(id, category.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}