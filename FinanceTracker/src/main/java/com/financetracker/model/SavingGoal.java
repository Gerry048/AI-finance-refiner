package com.financetracker.model;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class SavingGoal {
    private final String id; // 保持 final
    private String name;
    private BigDecimal targetAmount;
    private BigDecimal currentAmount; // 这个字段需要从文件加载
    private LocalDate targetDate;

    // --- 新增构造函数 (用于从存储加载) ---
    // 需要包含 currentAmount
    public SavingGoal(String id, String name, BigDecimal targetAmount, BigDecimal currentAmount, LocalDate targetDate) {
        Objects.requireNonNull(id, "ID cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(targetAmount, "Target amount cannot be null");
        // currentAmount 在加载时不能为空，因为它代表了存储的状态
        Objects.requireNonNull(currentAmount, "Current amount cannot be null when loading");
        // targetDate 可以为 null
        this.id = id;
        this.name = name;
        this.targetAmount = targetAmount;
        this.currentAmount = currentAmount; // 直接设置加载的值
        this.targetDate = targetDate;
    }


    // --- 修改原有的公共构造函数 ---
    // 用于创建全新的 SavingGoal 对象, currentAmount 初始为 0
    public SavingGoal(String name, BigDecimal targetAmount, LocalDate targetDate) {
        // 调用新构造函数，并将 currentAmount 初始化为 ZERO
        this(UUID.randomUUID().toString(), name, targetAmount, BigDecimal.ZERO, targetDate);
    }

    // Getters (保持不变)
    public String getId() { return id; }
    public String getName() { return name; }
    public BigDecimal getTargetAmount() { return targetAmount; }
    public BigDecimal getCurrentAmount() { return currentAmount; }
    public LocalDate getTargetDate() { return targetDate; }

    // Setters (保持不变，特别是 setCurrentAmount 需要保留以供加载时设置)
    public void setName(String name) { this.name = name; }
    public void setTargetAmount(BigDecimal targetAmount) { this.targetAmount = targetAmount; }
    public void addContribution(BigDecimal amount) { /* 保持不变 */
        if (amount != null && amount.compareTo(BigDecimal.ZERO) > 0) {
            this.currentAmount = this.currentAmount.add(amount);
        }
    }
    public void setCurrentAmount(BigDecimal currentAmount) { /* 保持不变 */
        this.currentAmount = (currentAmount == null) ? BigDecimal.ZERO : currentAmount;
    }
    public void setTargetDate(LocalDate targetDate) { this.targetDate = targetDate; }


    // equals(), hashCode(), toString() (保持不变, 基于 ID)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SavingGoal that = (SavingGoal) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "SavingGoal{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", targetAmount=" + targetAmount +
                ", currentAmount=" + currentAmount +
                ", targetDate=" + targetDate +
                '}';
    }
}