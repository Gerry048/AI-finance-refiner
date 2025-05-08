package com.financetracker.service.impl;

import com.financetracker.model.Category;
import com.financetracker.repository.DataStorage;
import com.financetracker.service.CategoryService;

import java.io.IOException;
import java.util.List;

public class CategoryServiceImpl implements CategoryService {
    private final DataStorage storage;

    public CategoryServiceImpl(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public Category addCategory(String name) {
        if (name == null || name.isBlank())
            throw new IllegalArgumentException("分类名不能为空");
        try {
            List<Category> all = storage.loadCategories();
            boolean exists = all.stream()
                    .anyMatch(c -> c.getName().equalsIgnoreCase(name));
            if (exists) throw new IllegalArgumentException("分类已存在");
            Category c = new Category(name);
            all.add(c);
            storage.saveCategories(all);
            return c;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Category> getAllCategories() {
        try {
            return storage.loadCategories();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Category getCategoryById(String id) {
        return getAllCategories().stream()
                .filter(c -> c.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public Category getCategoryByName(String name) {
        return getAllCategories().stream()
                .filter(c -> c.getName().equalsIgnoreCase(name))
                .findFirst().orElse(null);
    }

    @Override
    public Category updateCategory(Category category) {
        if (category == null || category.getId() == null)
            throw new IllegalArgumentException("分类无效");
        List<Category> all = getAllCategories();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(category.getId())) {
                all.set(i, category);
                try { storage.saveCategories(all); }
                catch(IOException e){ throw new RuntimeException(e); }
                return category;
            }
        }
        throw new IllegalArgumentException("未找到分类");
    }

    @Override
    public boolean deleteCategory(String id) {
        List<Category> all = getAllCategories();
        boolean removed = all.removeIf(c -> c.getId().equals(id));
        if (removed) {
            try { storage.saveCategories(all); }
            catch(IOException ignored) {}
        }
        return removed;
    }
}
