package com.financetracker.service;

import com.financetracker.model.Budget;
import com.financetracker.model.Category;

import java.math.BigDecimal;
import java.time.Month;
import java.util.List;

/**
 * 处理预算设置和查询的接口。
 */
public interface BudgetService {

    /**
     * 为特定分类、月份和年份设置或更新预算。
     * 如果已存在对应条目则更新，否则创建新条目。
     *
     * @param category 预算对应的分类。
     * @param amount   预算金额。
     * @param month    预算月份。
     * @param year     预算年份。
     * @return 设置或更新后的预算对象。
     * @throws IllegalArgumentException 如果参数无效 (例如 category或amount为null, 金额非正)。
     */
    Budget setBudget(Category category, BigDecimal amount, Month month, int year);

    /**
     * 获取特定分类、月份和年份的预算。
     *
     * @param category 预算对应的分类。
     * @param month    预算月份。
     * @param year     预算年份。
     * @return 找到的预算对象，如果未设置则返回 null 或 Optional.empty()。
     */
    Budget getBudget(Category category, Month month, int year); // 或 Optional<Budget>

    /**
     * 获取指定月份和年份的所有预算设置。
     *
     * @param month 月份。
     * @param year  年份。
     * @return 该月份的所有预算列表。
     */
    List<Budget> getBudgetsForMonth(Month month, int year);

    /**
     * 删除特定分类、月份和年份的预算设置。
     *
     * @param category 分类。
     * @param month    月份。
     * @param year     年份。
     * @return 删除成功返回 true，否则返回 false (例如未找到该预算)。
     */
    boolean deleteBudget(Category category, Month month, int year);

}