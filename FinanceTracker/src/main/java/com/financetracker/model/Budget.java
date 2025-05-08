package com.financetracker.model;

import java.math.BigDecimal;
import java.time.Month;
import java.util.Objects;
import java.util.UUID;

public class Budget {
    private final String id; // 保持 final
    private Category category;
    private BigDecimal amount;
    private Month month;
    private int year;

    // --- 新增构造函数 (用于从存储加载) ---
    public Budget(String id, Category category, BigDecimal amount, Month month, int year) {
        Objects.requireNonNull(id, "ID cannot be null");
        // category 可以为 null (如果允许为全局或未分类设置预算?) 确认业务逻辑
        Objects.requireNonNull(amount, "Amount cannot be null");
        Objects.requireNonNull(month, "Month cannot be null");
        // year 一般不会是 0 或负数，可以添加校验 if (year <= 0) ...
        this.id = id;
        this.category = category;
        this.amount = amount;
        this.month = month;
        this.year = year;
    }

    // --- 修改原有的公共构造函数 ---
    // 用于创建全新的 Budget 对象
    public Budget(Category category, BigDecimal amount, Month month, int year) {
        this(UUID.randomUUID().toString(), category, amount, month, year);
    }

    // Getters and Setters (保持不变)
    public String getId() { return id; }
    public Category getCategory() { return category; }
    public BigDecimal getAmount() { return amount; }
    public Month getMonth() { return month; }
    public int getYear() { return year; }
    public void setCategory(Category category) { this.category = category; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setMonth(Month month) { this.month = month; }
    public void setYear(int year) { this.year = year; }

    // equals(), hashCode(), toString() (保持不变)
    // 注意：这里的 equals 是基于 category, month, year，而不是 ID。确认这是你想要的逻辑。
    // 如果需要基于 ID 判断，需要修改 equals 和 hashCode。
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Budget budget = (Budget) o;
        // 当前逻辑：同一个月同一个分类的预算视为相同（不考虑ID）
        return year == budget.year && Objects.equals(category, budget.category) && month == budget.month;
        // 如果要基于ID判断： return Objects.equals(id, budget.id);
    }

    @Override
    public int hashCode() {
        // 对应 equals 的修改
        return Objects.hash(category, month, year);
        // 如果要基于ID判断： return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Budget{" +
                "id='" + id + '\'' +
                ", category=" + (category != null ? category.getName() : "null") +
                ", amount=" + amount +
                ", month=" + month +
                ", year=" + year +
                '}';
    }
}