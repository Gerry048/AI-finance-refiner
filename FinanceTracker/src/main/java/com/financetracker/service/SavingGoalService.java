package com.financetracker.service;

import com.financetracker.model.SavingGoal;

import java.math.BigDecimal;
import java.util.List;

/**
 * 处理储蓄目标设置、跟踪和管理的接口。
 */
public interface SavingGoalService {

    /**
     * 添加一个新的储蓄目标。
     *
     * @param goal 要添加的储蓄目标对象 (应包含名称, 目标金额, 目标日期[可选])。
     * @return 添加成功后的储蓄目标对象 (包含生成的ID和初始当前金额)。
     * @throws IllegalArgumentException 如果目标信息无效。
     */
    SavingGoal addSavingGoal(SavingGoal goal);

    /**
     * 获取所有的储蓄目标。
     *
     * @return 所有储蓄目标的列表。
     */
    List<SavingGoal> getAllSavingGoals();

    /**
     * 根据ID获取单个储蓄目标。
     *
     * @param goalId 目标ID。
     * @return 找到的目标对象，未找到则返回 null 或 Optional.empty()。
     */
    SavingGoal getSavingGoalById(String goalId); // 或 Optional<SavingGoal>

    /**
     * 更新储蓄目标信息 (名称, 目标金额, 目标日期)。
     * 注意：当前已存金额通常不直接通过此方法更新，而是通过 addContribution。
     *
     * @param goal 包含更新信息的储蓄目标对象 (必须有有效的ID)。
     * @return 更新后的储蓄目标对象。
     * @throws IllegalArgumentException 如果目标对象或ID无效，或未找到。
     */
    SavingGoal updateSavingGoal(SavingGoal goal);

    /**
     * 向指定的储蓄目标增加一笔存款贡献。
     *
     * @param goalId 要增加贡献的目标ID。
     * @param amount 贡献的金额。
     * @return 更新当前金额后的储蓄目标对象。
     * @throws IllegalArgumentException 如果 goalId 无效，或未找到，或 amount 无效(null或非正数)。
     */
    SavingGoal addContribution(String goalId, BigDecimal amount);

    /**
     * 根据ID删除一个储蓄目标。
     *
     * @param goalId 要删除的目标ID。
     * @return 删除成功返回 true，否则返回 false。
     */
    boolean deleteSavingGoal(String goalId);
}