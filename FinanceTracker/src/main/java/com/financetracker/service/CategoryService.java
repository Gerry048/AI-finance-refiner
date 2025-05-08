package com.financetracker.service;


import com.financetracker.model.Category;

import java.util.List;

/**
 * 处理分类相关业务逻辑的接口。
 */
public interface CategoryService {

    /**
     * 添加一个新的分类。
     *
     * @param categoryName 新分类的名称。
     * @return 创建成功的分类对象 (包含生成的ID)。
     * @throws IllegalArgumentException 如果分类名称为空或已存在。
     */
    Category addCategory(String categoryName);

    /**
     * 获取所有已定义的分类。
     *
     * @return 包含所有分类对象的列表。
     */
    List<Category> getAllCategories();

    /**
     * 根据ID获取分类。
     *
     * @param categoryId 分类ID。
     * @return 找到的分类对象，未找到则返回 null 或 Optional.empty()。
     */
    Category getCategoryById(String categoryId); // 或 Optional<Category>

    /**
     * 根据名称获取分类 (假设分类名称是唯一的)。
     *
     * @param name 分类名称。
     * @return 找到的分类对象，未找到则返回 null 或 Optional.empty()。
     */
    Category getCategoryByName(String name); // 或 Optional<Category>

    /**
     * 更新分类信息 (例如修改名称)。
     *
     * @param category 包含更新信息的分类对象 (必须有有效的ID)。
     * @return 更新后的分类对象。
     * @throws IllegalArgumentException 如果分类对象或ID无效，或未找到，或新名称冲突。
     */
    Category updateCategory(Category category);

    /**
     * 根据ID删除一个分类。
     * 注意：需要考虑该分类是否已被交易使用。实现时可能需要决定策略
     * (例如：不允许删除被使用的分类，或将使用该分类的交易设为"未分类")。
     *
     * @param categoryId 要删除的分类ID。
     * @return 删除成功返回 true，否则返回 false。
     * @throws IllegalStateException 如果尝试删除一个正在被使用的分类 (这是一种可能的实现策略)。
     */
    boolean deleteCategory(String categoryId);
}