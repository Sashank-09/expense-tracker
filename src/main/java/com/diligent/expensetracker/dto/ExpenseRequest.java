package com.diligent.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * Payload accepted by POST /api/expenses.
 * The id is server-generated, so it is intentionally excluded here.
 */
@Schema(description = "Request object used to create a new expense")
public class ExpenseRequest {

    @Schema(
            description = "Title or description of the expense",
            example = "Lunch at Restaurant"
    )
    @NotBlank(message = "title is required")
    private String title;

    @Schema(
            description = "Expense amount",
            example = "450.75"
    )
    @NotNull(message = "amount is required")
    @DecimalMin(value = "0.0", inclusive = false,
            message = "amount must be greater than 0")
    private BigDecimal amount;

    @Schema(
            description = "Expense category",
            example = "Food"
    )
    @NotBlank(message = "category is required")
    private String category;

    @Schema(
            description = "Date of the expense",
            example = "2026-07-31"
    )
    @NotNull(message = "date is required")
    private LocalDate date;

    public ExpenseRequest() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}