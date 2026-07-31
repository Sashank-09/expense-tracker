package com.diligent.expensetracker.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.util.Map;

/**
 * Response body for GET /api/expenses/total.
 * "category" is null when the total is calculated across all expenses.
 */
@Schema(description = "Response containing expense totals")
public class TotalResponse {

    @Schema(
            description = "Requested category. Null when returning totals for all categories.",
            example = "Food"
    )
    private String category;

    @Schema(
            description = "Total expense amount",
            example = "2350.50"
    )
    private BigDecimal total;

    @Schema(
            description = "Expense totals grouped by category"
    )
    private Map<String, BigDecimal> byCategory;

    public TotalResponse() {
    }

    public TotalResponse(String category,
                         BigDecimal total,
                         Map<String, BigDecimal> byCategory) {
        this.category = category;
        this.total = total;
        this.byCategory = byCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getTotal() {
        return total;
    }

    public void setTotal(BigDecimal total) {
        this.total = total;
    }

    public Map<String, BigDecimal> getByCategory() {
        return byCategory;
    }

    public void setByCategory(Map<String, BigDecimal> byCategory) {
        this.byCategory = byCategory;
    }
}