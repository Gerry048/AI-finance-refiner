package com.financetracker.service.impl;

import com.financetracker.model.Category;
import com.financetracker.model.Transaction;
import com.financetracker.repository.DataStorage;
import com.financetracker.service.TransactionService;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TransactionServiceImpl implements TransactionService {
    private final DataStorage storage;

    public TransactionServiceImpl(DataStorage storage) {
        this.storage = storage;
    }

    @Override
    public Transaction addTransaction(Transaction tx) {
        if (tx == null) throw new IllegalArgumentException("交易不能为空");
        try {
            List<Transaction> all = storage.loadTransactions();
            all.add(tx);
            storage.saveTransactions(all);
            return tx;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {
        try {
            return storage.loadTransactions();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Transaction getTransactionById(String id) {
        return getAllTransactions().stream()
                .filter(t -> t.getId().equals(id))
                .findFirst().orElse(null);
    }

    @Override
    public List<Transaction> getTransactionsByCategory(Category category) {
        if (category == null) throw new IllegalArgumentException("分类不能为空");
        return getAllTransactions().stream()
                .filter(t -> category.equals(t.getCategory()))
                .collect(Collectors.toList());
    }

    @Override
    public List<Transaction> getTransactionsByDateRange(LocalDate start, LocalDate end) {
        if (start == null || end == null || start.isAfter(end))
            throw new IllegalArgumentException("日期范围不正确");
        return getAllTransactions().stream()
                .filter(t -> !t.getDate().isBefore(start) && !t.getDate().isAfter(end))
                .collect(Collectors.toList());
    }

    @Override
    public Transaction updateTransaction(Transaction tx) {
        if (tx == null || tx.getId() == null)
            throw new IllegalArgumentException("交易或ID不能为空");
        List<Transaction> all = getAllTransactions();
        for (int i = 0; i < all.size(); i++) {
            if (all.get(i).getId().equals(tx.getId())) {
                all.set(i, tx);
                try { storage.saveTransactions(all); }
                catch(IOException e){ throw new RuntimeException(e); }
                return tx;
            }
        }
        throw new IllegalArgumentException("未找到要更新的交易");
    }

    @Override
    public boolean deleteTransaction(String id) {
        List<Transaction> all = getAllTransactions();
        boolean removed = all.removeIf(t -> t.getId().equals(id));
        if (removed) {
            try { storage.saveTransactions(all); }
            catch(IOException ignored) {}
        }
        return removed;
    }

    @Override
    public Map<Category, BigDecimal> getTotalSpendingByCategory(LocalDate start, LocalDate end) {
        return getTransactionsByDateRange(start, end).stream()
                .filter(t -> t.getAmount() != null)
                .collect(Collectors.groupingBy(
                        Transaction::getCategory,
                        Collectors.mapping(
                                Transaction::getAmount,
                                Collectors.reducing(BigDecimal.ZERO, BigDecimal::add)
                        )
                ));
    }
}
