package com.diligent.expensetracker.service;

import com.diligent.expensetracker.dto.ExpenseRequest;
import com.diligent.expensetracker.exception.ExpenseNotFoundException;
import com.diligent.expensetracker.model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseServiceTest {

    private ExpenseService service;

    @BeforeEach
    void setUp() {
        service = new ExpenseService();
    }

    private ExpenseRequest request(String title, String amount, String category, String date) {
        ExpenseRequest r = new ExpenseRequest();
        r.setTitle(title);
        r.setAmount(new BigDecimal(amount));
        r.setCategory(category);
        r.setDate(LocalDate.parse(date));
        return r;
    }

    @Test
    void addExpense_assignsIncrementingIds() {
        Expense first = service.addExpense(request("Coffee", "150.00", "Food", "2026-07-01"));
        Expense second = service.addExpense(request("Bus ticket", "40.00", "Travel", "2026-07-02"));

        assertEquals(1L, first.getId());
        assertEquals(2L, second.getId());
    }

    @Test
    void getAllExpenses_returnsAllInInsertionOrder() {
        service.addExpense(request("Coffee", "150.00", "Food", "2026-07-01"));
        service.addExpense(request("Bus ticket", "40.00", "Travel", "2026-07-02"));

        List<Expense> all = service.getAllExpenses();

        assertEquals(2, all.size());
        assertEquals("Coffee", all.get(0).getTitle());
        assertEquals("Bus ticket", all.get(1).getTitle());
    }

    @Test
    void getExpensesByCategory_filtersCaseInsensitively() {
        service.addExpense(request("Coffee", "150.00", "Food", "2026-07-01"));
        service.addExpense(request("Groceries", "800.00", "food", "2026-07-03"));
        service.addExpense(request("Bus ticket", "40.00", "Travel", "2026-07-02"));

        List<Expense> foodExpenses = service.getExpensesByCategory("FOOD");

        assertEquals(2, foodExpenses.size());
    }

    @Test
    void getExpensesByCategory_noMatches_returnsEmptyList() {
        service.addExpense(request("Coffee", "150.00", "Food", "2026-07-01"));

        List<Expense> result = service.getExpensesByCategory("Entertainment");

        assertTrue(result.isEmpty());
    }

    @Test
    void deleteExpense_removesIt() {
        Expense created = service.addExpense(request("Coffee", "150.00", "Food", "2026-07-01"));

        service.deleteExpense(created.getId());

        assertTrue(service.getAllExpenses().isEmpty());
    }

    @Test
    void deleteExpense_unknownId_throws() {
        assertThrows(ExpenseNotFoundException.class, () -> service.deleteExpense(999L));
    }

    @Test
    void getExpenseById_unknownId_throws() {
        assertThrows(ExpenseNotFoundException.class, () -> service.getExpenseById(999L));
    }

    @Test
    void getTotal_sumsAllExpenses() {
        service.addExpense(request("Coffee", "150.50", "Food", "2026-07-01"));
        service.addExpense(request("Bus ticket", "40.25", "Travel", "2026-07-02"));

        BigDecimal total = service.getTotal();

        assertEquals(0, new BigDecimal("190.75").compareTo(total));
    }

    @Test
    void getTotal_noExpenses_returnsZero() {
        assertEquals(0, BigDecimal.ZERO.compareTo(service.getTotal()));
    }

    @Test
    void getTotalByCategory_sumsOnlyMatchingCategory() {
        service.addExpense(request("Coffee", "150.00", "Food", "2026-07-01"));
        service.addExpense(request("Groceries", "800.00", "Food", "2026-07-03"));
        service.addExpense(request("Bus ticket", "40.00", "Travel", "2026-07-02"));

        BigDecimal foodTotal = service.getTotalByCategory("Food");

        assertEquals(0, new BigDecimal("950.00").compareTo(foodTotal));
    }

    @Test
    void getTotalsGroupedByCategory_groupsCorrectly() {
        service.addExpense(request("Coffee", "150.00", "Food", "2026-07-01"));
        service.addExpense(request("Groceries", "800.00", "Food", "2026-07-03"));
        service.addExpense(request("Bus ticket", "40.00", "Travel", "2026-07-02"));

        Map<String, BigDecimal> grouped = service.getTotalsGroupedByCategory();

        assertEquals(0, new BigDecimal("950.00").compareTo(grouped.get("Food")));
        assertEquals(0, new BigDecimal("40.00").compareTo(grouped.get("Travel")));
    }
}
