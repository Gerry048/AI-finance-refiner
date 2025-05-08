package com.financetracker.repository;

import com.financetracker.model.*; // 导入所有模型类

import java.io.IOException; // 文件操作可能抛出IO异常
import java.util.List;

/**
 * 数据存储接口，定义了与持久化层（例如文本文件）交互的方法。
 * 实现这个接口的类将负责实际的数据读写操作。
 */
public interface DataStorage {

    /**
     * 从持久化存储中加载所有交易记录。
     *
     * @return 包含所有交易记录的列表。如果存储中没有数据或文件不存在，应返回空列表。
     * @throws IOException 如果在读取数据时发生 I/O 错误。
     */
    List<Transaction> loadTransactions() throws IOException;

    /**
     * 将当前的交易记录列表保存到持久化存储中。
     * 这通常会覆盖之前存储的所有交易数据。
     *
     * @param transactions 要保存的交易记录列表。
     * @throws IOException 如果在写入数据时发生 I/O 错误。
     */
    void saveTransactions(List<Transaction> transactions) throws IOException;

    /**
     * 从持久化存储中加载所有分类信息。
     *
     * @return 包含所有分类信息的列表。如果存储中没有数据或文件不存在，应返回空列表。
     * @throws IOException 如果在读取数据时发生 I/O 错误。
     */
    List<Category> loadCategories() throws IOException;

    /**
     * 将当前的分类列表保存到持久化存储中。
     *
     * @param categories 要保存的分类列表。
     * @throws IOException 如果在写入数据时发生 I/O 错误。
     */
    void saveCategories(List<Category> categories) throws IOException;

    /**
     * 从持久化存储中加载所有用户信息。
     * (即使当前设计为单用户，接口设计成列表更具扩展性)
     *
     * @return 包含所有用户信息的列表。
     * @throws IOException 如果在读取数据时发生 I/O 错误。
     */
    List<User> loadUsers() throws IOException;

    /**
     * 将当前的用户列表保存到持久化存储中。
     *
     * @param users 要保存的用户列表。
     * @throws IOException 如果在写入数据时发生 I/O 错误。
     */
    void saveUsers(List<User> users) throws IOException;

    /**
     * 从持久化存储中加载所有预算设置。
     *
     * @return 包含所有预算设置的列表。
     * @throws IOException 如果在读取数据时发生 I/O 错误。
     */
    List<Budget> loadBudgets() throws IOException;

    /**
     * 将当前的预算设置列表保存到持久化存储中。
     *
     * @param budgets 要保存的预算列表。
     * @throws IOException 如果在写入数据时发生 I/O 错误。
     */
    void saveBudgets(List<Budget> budgets) throws IOException;

    /**
     * 从持久化存储中加载所有储蓄目标。
     *
     * @return 包含所有储蓄目标的列表。
     * @throws IOException 如果在读取数据时发生 I/O 错误。
     */
    List<SavingGoal> loadSavingGoals() throws IOException;

    /**
     * 将当前的储蓄目标列表保存到持久化存储中。
     *
     * @param savingGoals 要保存的储蓄目标列表。
     * @throws IOException 如果在写入数据时发生 I/O 错误。
     */
    void saveSavingGoals(List<SavingGoal> savingGoals) throws IOException;

}