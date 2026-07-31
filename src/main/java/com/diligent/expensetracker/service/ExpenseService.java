package com.diligent.expensetracker.service;

import com.diligent.expensetracker.dto.ExpenseRequest;
import com.diligent.expensetracker.exception.ExpenseNotFoundException;
import com.diligent.expensetracker.model.Expense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * In-memory store for expenses.
 * A ConcurrentHashMap is used instead of a plain HashMap/ArrayList
 * so the API is safe under concurrent requests without extra
 * synchronization code.
 */
@Service
public class ExpenseService {

    private static final Logger logger =
            LoggerFactory.getLogger(ExpenseService.class);

    private final Map<Long, Expense> expenses = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    /**
     * Adds a new expense.
     */
    public Expense addExpense(ExpenseRequest request) {

        Long id = idGenerator.getAndIncrement();

        Expense expense = new Expense(
                id,
                request.getTitle(),
                request.getAmount(),
                request.getCategory(),
                request.getDate()
        );

        expenses.put(id, expense);

        logger.info(
                "Expense created successfully. ID={}, Title={}, Amount={}, Category={}",
                expense.getId(),
                expense.getTitle(),
                expense.getAmount(),
                expense.getCategory()
        );

        return expense;
    }

    /**
     * Returns all expenses sorted by ID.
     */
    public List<Expense> getAllExpenses() {

        logger.debug("Retrieving all expenses.");

        return expenses.values()
                .stream()
                .sorted(Comparator.comparing(Expense::getId))
                .collect(Collectors.toList());
    }

    /**
     * Returns all expenses belonging to a category.
     */
    public List<Expense> getExpensesByCategory(String category) {

        logger.debug("Retrieving expenses for category '{}'.", category);

        return expenses.values()
                .stream()
                .filter(e -> e.getCategory().equalsIgnoreCase(category))
                .sorted(Comparator.comparing(Expense::getId))
                .collect(Collectors.toList());
    }

    /**
     * Returns an expense by its ID.
     */
    public Expense getExpenseById(Long id) {

        Expense expense = expenses.get(id);

        if (expense == null) {

            logger.warn("Expense not found. ID={}", id);

            throw new ExpenseNotFoundException(id);
        }

        logger.debug("Retrieved expense. ID={}", id);

        return expense;
    }

    /**
     * Deletes an expense by ID.
     */
    public void deleteExpense(Long id) {

        if (!expenses.containsKey(id)) {

            logger.warn("Attempted to delete non-existing expense. ID={}", id);

            throw new ExpenseNotFoundException(id);
        }

        expenses.remove(id);

        logger.info("Expense deleted successfully. ID={}", id);
    }

    /**
     * Calculates total of all expenses.
     */
    public BigDecimal getTotal() {

        logger.debug("Calculating total expenses.");

        return expenses.values()
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Calculates total for a specific category.
     */
    public BigDecimal getTotalByCategory(String category) {

        logger.debug("Calculating total for category '{}'.", category);

        return getExpensesByCategory(category)
                .stream()
                .map(Expense::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    /**
     * Returns totals grouped by category.
     * Example:
     * {
     *   "Food": 450.00,
     *   "Travel": 1200.00
     * }
     */
    public Map<String, BigDecimal> getTotalsGroupedByCategory() {

        logger.debug("Calculating total expenses grouped by category.");

        Map<String, BigDecimal> totals = new LinkedHashMap<>();

        for (Expense expense : getAllExpenses()) {
            totals.merge(
                    expense.getCategory(),
                    expense.getAmount(),
                    BigDecimal::add
            );
        }

        return totals;
    }
}