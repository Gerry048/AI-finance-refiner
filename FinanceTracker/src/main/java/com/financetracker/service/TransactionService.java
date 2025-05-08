package com.financetracker.service;

import com.financetracker.model.Category;
import com.financetracker.model.Transaction;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map; // 可能用于返回按类别汇总的金额

/**
 * 处理交易相关业务逻辑的接口。
 */
public interface TransactionService {

    /**
     * 添加一笔新的交易记录。
     *
     * @param transaction 要添加的交易对象 (应包含日期, 金额, 分类[可选], 描述[可选])
     * @return 添加成功后的交易对象 (可能包含由系统生成的ID)
     * @throws IllegalArgumentException 如果交易信息无效 (例如金额为null或非正数)
     */
    Transaction addTransaction(Transaction transaction);

    /**
     * 获取所有的交易记录。
     * (未来可能需要添加用户ID参数，如果支持多用户)
     *
     * @return 包含所有交易记录的列表，如果没有任何交易则返回空列表。
     */
    List<Transaction> getAllTransactions();

    /**
     * 根据ID获取单个交易记录。
     *
     * @param transactionId 要查找的交易ID。
     * @return 找到的交易对象，如果未找到则返回 null 或 Optional.empty() (设计选择)。
     */
    Transaction getTransactionById(String transactionId); // 或者返回 Optional<Transaction>

    /**
     * 获取指定分类下的所有交易记录。
     *
     * @param category 指定的分类对象。
     * @return 该分类下的交易记录列表，如果该分类下无交易则返回空列表。
     * @throws IllegalArgumentException 如果 category 为 null。
     */
    List<Transaction> getTransactionsByCategory(Category category);

    /**
     * 获取指定日期范围内的所有交易记录。
     *
     * @param startDate 开始日期 (包含)。
     * @param endDate   结束日期 (包含)。
     * @return 该日期范围内的交易记录列表。
     * @throws IllegalArgumentException 如果日期为null或开始日期在结束日期之后。
     */
    List<Transaction> getTransactionsByDateRange(LocalDate startDate, LocalDate endDate);

    /**
     * 更新现有的交易记录。
     * 主要用于修改金额、日期、分类、描述等，例如 AI 修正分类后。
     *
     * @param transaction 包含更新信息的交易对象 (必须有有效的ID)。
     * @return 更新成功后的交易对象。
     * @throws IllegalArgumentException 如果交易对象或其ID无效，或者未找到要更新的交易。
     */
    Transaction updateTransaction(Transaction transaction);

    /**
     * 根据ID删除一笔交易记录。
     *
     * @param transactionId 要删除的交易ID。
     * @return 如果删除成功返回 true，否则返回 false (例如未找到该ID)。
     */
    boolean deleteTransaction(String transactionId);

    /**
     * 计算指定日期范围内，按类别汇总的总支出。
     * (这是一个示例性的分析方法，可以根据需要添加更多)
     *
     * @param startDate 开始日期。
     * @param endDate   结束日期。
     * @return 一个Map，键是Category对象，值是该类别在指定日期范围内的总支出金额。
     */
    Map<Category, BigDecimal> getTotalSpendingByCategory(LocalDate startDate, LocalDate endDate);

}

