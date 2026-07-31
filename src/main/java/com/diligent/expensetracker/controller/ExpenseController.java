package com.diligent.expensetracker.controller;

import com.diligent.expensetracker.dto.ExpenseRequest;
import com.diligent.expensetracker.dto.TotalResponse;
import com.diligent.expensetracker.model.Expense;
import com.diligent.expensetracker.service.ExpenseService;

import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.util.List;


@Tag(
	    name = "Expense Management",
	    description = "Operations for managing personal expenses"
	)
@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }
    
    @Operation(
    	    summary = "Create a new expense",
    	    description = "Creates a new expense using the supplied request details."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "201", description = "Expense created successfully"),
    	    @ApiResponse(responseCode = "400", description = "Invalid request payload")
    })

    @PostMapping
    public ResponseEntity<Expense> addExpense(@Valid @RequestBody ExpenseRequest request) {
        Expense created = expenseService.addExpense(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    /**
     * GET /api/expenses            -> all expenses
     * GET /api/expenses?category=X -> expenses filtered by category
     */
    @Operation(
    	    summary = "Retrieve all expenses",
    	    description = "Returns all recorded expenses sorted by date."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Expenses retrieved successfully")
    	})
    @GetMapping
    public ResponseEntity<List<Expense>> getExpenses(
            @RequestParam(required = false) String category) {
        List<Expense> result = (category == null || category.isBlank())
                ? expenseService.getAllExpenses()
                : expenseService.getExpensesByCategory(category);
        return ResponseEntity.ok(result);
    }
    
    
    @Operation(
    	    summary = "Retrieve an expense by ID",
    	    description = "Returns a single expense using its unique identifier."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Expense found"),
    	    @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Expense> getExpenseById(@PathVariable Long id) {
        return ResponseEntity.ok(expenseService.getExpenseById(id));
    }

    /**
     * GET /api/expenses/total            -> overall total + breakdown by category
     * GET /api/expenses/total?category=X -> total for a single category
     */
    @Operation(
    	    summary = "Calculate total expenses",
    	    description = "Returns the total amount of all recorded expenses."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "200", description = "Total calculated successfully")
    })
    @GetMapping("/total")
    public ResponseEntity<TotalResponse> getTotal(
            @RequestParam(required = false) String category) {
        if (category == null || category.isBlank()) {
            TotalResponse response = new TotalResponse(null, expenseService.getTotal(),
                    expenseService.getTotalsGroupedByCategory());
            return ResponseEntity.ok(response);
        }
        TotalResponse response = new TotalResponse(category, expenseService.getTotalByCategory(category), null);
        return ResponseEntity.ok(response);
    }
    
    @Operation(
    	    summary = "Delete an expense",
    	    description = "Deletes an expense using its unique identifier."
    	)
    	@ApiResponses({
    	    @ApiResponse(responseCode = "204", description = "Expense deleted successfully"),
    	    @ApiResponse(responseCode = "404", description = "Expense not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        expenseService.deleteExpense(id);
        return ResponseEntity.noContent().build();
    }
}
