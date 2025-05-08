package com.financetracker.repository;

import com.financetracker.model.*;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 基于 CSV 的持久化，每个用户一个目录：data/{userId}/*.csv
 */
public class CsvDataStorage implements DataStorage {

    private static final String ROOT_DIR = "data";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE;

    private static final String[] CATEGORY_HEADER      = {"id", "name"};
    private static final String[] TRANSACTION_HEADER   = {"id", "date", "amount", "categoryId", "description"};
    private static final String[] USER_HEADER          = {"id", "username", "passwordHash", "email"};
    private static final String[] BUDGET_HEADER        = {"id", "categoryId", "amount", "month", "year"};
    private static final String[] SAVING_GOAL_HEADER   = {"id", "name", "targetAmount", "currentAmount", "targetDate"};

    private final File categoriesFile;
    private final File transactionsFile;
    private final File usersFile;
    private final File budgetsFile;
    private final File savingGoalsFile;

    // 缓存分类，按需加载
    private Map<String, Category> categoriesById = new HashMap<>();

    public CsvDataStorage(String userId) {
        File userDir = new File(ROOT_DIR, userId);
        if (!userDir.exists()) userDir.mkdirs();

        categoriesFile    = new File(userDir, "categories.csv");
        transactionsFile  = new File(userDir, "transactions.csv");
        usersFile         = new File(userDir, "users.csv");
        budgetsFile       = new File(userDir, "budgets.csv");
        savingGoalsFile   = new File(userDir, "saving_goals.csv");

        initCsv(categoriesFile, CATEGORY_HEADER);
        initCsv(transactionsFile, TRANSACTION_HEADER);
        initCsv(usersFile, USER_HEADER);
        initCsv(budgetsFile, BUDGET_HEADER);
        initCsv(savingGoalsFile, SAVING_GOAL_HEADER);

        try {
            loadCategoriesInternal();
        } catch (IOException e) {
            System.err.println("警告：初始化加载分类失败—" + e.getMessage());
        }
    }

    private void initCsv(File file, String[] header) {
        if (!file.exists()) {
            try (CSVPrinter printer = new CSVPrinter(
                    new FileWriter(file),
                    CSVFormat.DEFAULT.builder().setHeader(header).build()
            )) {
                // 仅写入表头
            } catch (IOException e) {
                System.err.println("无法初始化文件 " + file + ": " + e.getMessage());
            }
        }
    }

    // ================== Category ==================

    @Override
    public List<Category> loadCategories() throws IOException {
        if (categoriesById.isEmpty()) loadCategoriesInternal();
        return new ArrayList<>(categoriesById.values());
    }

    private void loadCategoriesInternal() throws IOException {
        categoriesById.clear();
        List<Category> list = loadData(categoriesFile.getPath(), CATEGORY_HEADER, this::parseCategory);
        categoriesById = list.stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));
    }

    @Override
    public void saveCategories(List<Category> categories) throws IOException {
        saveData(categoriesFile.getPath(), CATEGORY_HEADER, categories, this::categoryToRecord);
        categoriesById = categories.stream()
                .collect(Collectors.toMap(Category::getId, Function.identity()));
    }

    private Category parseCategory(CSVRecord r) {
        return new Category(r.get("id"), r.get("name"));
    }

    private List<String> categoryToRecord(Category c) {
        return Arrays.asList(c.getId(), c.getName());
    }

    // ================== Transaction ==================

    @Override
    public List<Transaction> loadTransactions() throws IOException {
        return loadData(transactionsFile.getPath(), TRANSACTION_HEADER, this::parseTransaction);
    }

    @Override
    public void saveTransactions(List<Transaction> txs) throws IOException {
        saveData(transactionsFile.getPath(), TRANSACTION_HEADER, txs, this::transactionToRecord);
    }

    private Transaction parseTransaction(CSVRecord r) {
        String id = r.get("id");
        LocalDate date;
        try {
            date = LocalDate.parse(r.get("date"), DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            return null;
        }
        BigDecimal amt;
        try {
            amt = new BigDecimal(r.get("amount"));
        } catch (NumberFormatException e) {
            return null;
        }
        String catId = r.get("categoryId");
        Category cat = categoriesById.get(catId);
        return new Transaction(id, date, amt, cat, r.get("description"));
    }

    private List<String> transactionToRecord(Transaction t) {
        return Arrays.asList(
                t.getId(),
                t.getDate().format(DATE_FORMATTER),
                t.getAmount().toPlainString(),
                t.getCategory() != null ? t.getCategory().getId() : "",
                t.getDescription() != null ? t.getDescription() : ""
        );
    }

    // ================== User ==================

    @Override
    public List<User> loadUsers() throws IOException {
        return loadData(usersFile.getPath(), USER_HEADER, this::parseUser);
    }

    @Override
    public void saveUsers(List<User> users) throws IOException {
        saveData(usersFile.getPath(), USER_HEADER, users, this::userToRecord);
    }

    private User parseUser(CSVRecord r) {
        return new User(
                r.get("id"),
                r.get("username"),
                r.get("passwordHash"),
                r.get("email")
        );
    }

    private List<String> userToRecord(User u) {
        return Arrays.asList(
                u.getId(),
                u.getUsername(),
                u.getPasswordHash(),
                u.getEmail() != null ? u.getEmail() : ""
        );
    }

    // ================== Budget ==================

    @Override
    public List<Budget> loadBudgets() throws IOException {
        return loadData(budgetsFile.getPath(), BUDGET_HEADER, this::parseBudget);
    }

    @Override
    public void saveBudgets(List<Budget> budgets) throws IOException {
        saveData(budgetsFile.getPath(), BUDGET_HEADER, budgets, this::budgetToRecord);
    }

    private Budget parseBudget(CSVRecord r) {
        try {
            BigDecimal amt = new BigDecimal(r.get("amount"));
            Month m = Month.valueOf(r.get("month").toUpperCase());
            int y = Integer.parseInt(r.get("year"));
            Category cat = categoriesById.get(r.get("categoryId"));
            return new Budget(r.get("id"), cat, amt, m, y);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> budgetToRecord(Budget b) {
        return Arrays.asList(
                b.getId(),
                b.getCategory() != null ? b.getCategory().getId() : "",
                b.getAmount().toPlainString(),
                b.getMonth().name(),
                String.valueOf(b.getYear())
        );
    }

    // ================== SavingGoal ==================

    @Override
    public List<SavingGoal> loadSavingGoals() throws IOException {
        return loadData(savingGoalsFile.getPath(), SAVING_GOAL_HEADER, this::parseSavingGoal);
    }

    @Override
    public void saveSavingGoals(List<SavingGoal> goals) throws IOException {
        saveData(savingGoalsFile.getPath(), SAVING_GOAL_HEADER, goals, this::savingGoalToRecord);
    }

    private SavingGoal parseSavingGoal(CSVRecord r) {
        try {
            BigDecimal target = new BigDecimal(r.get("targetAmount"));
            BigDecimal current = new BigDecimal(r.get("currentAmount"));
            LocalDate date = r.get("targetDate").isEmpty()
                    ? null
                    : LocalDate.parse(r.get("targetDate"), DATE_FORMATTER);
            return new SavingGoal(r.get("id"), r.get("name"), target, current, date);
        } catch (Exception e) {
            return null;
        }
    }

    private List<String> savingGoalToRecord(SavingGoal g) {
        return Arrays.asList(
                g.getId(),
                g.getName(),
                g.getTargetAmount().toPlainString(),
                g.getCurrentAmount().toPlainString(),
                g.getTargetDate() != null ? g.getTargetDate().format(DATE_FORMATTER) : ""
        );
    }

    // ================== 通用 CSV 读写 ==================

    private <T> List<T> loadData(String path, String[] headers, Function<org.apache.commons.csv.CSVRecord, T> parser)
            throws IOException {
        List<T> list = new ArrayList<>();
        File f = new File(path);
        if (!f.exists() || f.length() == 0) return list;
        try (Reader in = new FileReader(f);
             CSVParser csv = CSVFormat.DEFAULT
                     .builder()
                     .setHeader(headers)
                     .setSkipHeaderRecord(true)
                     .setTrim(true)
                     .build()
                     .parse(in)) {
            for (CSVRecord r : csv) {
                try {
                    T obj = parser.apply(r);
                    if (obj != null) list.add(obj);
                } catch (Exception ignored) {
                }
            }
        }
        return list;
    }

    private <T> void saveData(String path, String[] headers, List<T> data, Function<T, List<String>> writer)
            throws IOException {
        File f = new File(path);
        try (Writer out = new FileWriter(f);
             CSVPrinter p = new CSVPrinter(out,
                     CSVFormat.DEFAULT.builder().setHeader(headers).build())) {
            for (T item : data) {
                p.printRecord(writer.apply(item));
            }
        }
    }
}
