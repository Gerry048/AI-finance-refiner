package com.financetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Transaction {
    private final String id; // 保持 final
    private LocalDate date;
    private BigDecimal amount;
    private Category category;
    private String description;

    // --- 新增构造函数 (用于从存储加载) ---
    public Transaction(String id, LocalDate date, BigDecimal amount, Category category, String description) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(date, "Date cannot be null");
        Objects.requireNonNull(amount, "Amount cannot be null");
        // category 和 description 可以为 null
        this.id = id;
        this.date = date;
        this.amount = amount;
        this.category = category;
        this.description = description;
    }

    // --- 修改原有的公共构造函数 ---
    // 用于创建全新的 Transaction 对象
    public Transaction(LocalDate date, BigDecimal amount, Category category, String description) {
        this(UUID.randomUUID().toString(), date, amount, category, description);
    }

    // Getters and Setters (保持不变)
    public String getId() { return id; }
    public LocalDate getDate() { return date; }
    public BigDecimal getAmount() { return amount; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public void setDate(LocalDate date) { this.date = date; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setCategory(Category category) { this.category = category; }
    public void setDescription(String description) { this.description = description; }


    // equals(), hashCode(), toString() (保持不变, 基于 ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id='" + id + '\'' +
                ", date=" + date +
                ", amount=" + amount +
                ", category=" + (category != null ? category.getName() : "未分类") +
                ", description='" + description + '\'' +
                '}';
    }
}