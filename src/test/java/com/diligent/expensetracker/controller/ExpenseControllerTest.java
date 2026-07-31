package com.diligent.expensetracker.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Full-stack tests (controller -> service -> in-memory store) using MockMvc.
 * Each test builds its own state via the API rather than sharing fixtures,
 * since ExpenseService is a singleton bean and state persists across tests
 * within the same Spring context.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ExpenseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());

    private Map<String, Object> expensePayload(String title, String amount, String category, String date) {
        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("title", title);
        payload.put("amount", amount);
        payload.put("category", category);
        payload.put("date", date);
        return payload;
    }

    @Test
    void addExpense_validPayload_returns201WithBody() throws Exception {
        Map<String, Object> payload = expensePayload("Movie night", "500.00", "Entertainment", "2026-07-15");

        mockMvc.perform(post("/api/expenses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", notNullValue()))
                .andExpect(jsonPath("$.title", is("Movie night")))
                .andExpect(jsonPath("$.category", is("Entertainment")));
    }

    @Test
    void addExpense_missingTitle_returns400() throws Exception {
        Map<String, Object> payload = expensePayload("", "500.00", "Entertainment", "2026-07-15");

        mockMvc.perform(post("/api/expenses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Request validation failed"))
                .andExpect(jsonPath("$.fieldErrors[0].field").value("title"))
                .andExpect(jsonPath("$.fieldErrors[0].message").value("title is required"));
    }

    @Test
    void addExpense_negativeAmount_returns400() throws Exception {
        Map<String, Object> payload = expensePayload("Refund", "-10.00", "Misc", "2026-07-15");

        mockMvc.perform(post("/api/expenses")
                        .contentType("application/json")
                        .content(objectMapper.writeValueAsString(payload)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getExpenses_filtersByCategory() throws Exception {
        mockMvc.perform(post("/api/expenses").contentType("application/json")
                .content(objectMapper.writeValueAsString(
                        expensePayload("Metro card", "100.00", "TravelCategoryTest", "2026-07-05"))));
        mockMvc.perform(post("/api/expenses").contentType("application/json")
                .content(objectMapper.writeValueAsString(
                        expensePayload("Lunch", "200.00", "FoodCategoryTest", "2026-07-06"))));

        mockMvc.perform(get("/api/expenses").param("category", "TravelCategoryTest"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].category", is("TravelCategoryTest")));
    }

    @Test
    void deleteExpense_thenGetById_returns404() throws Exception {
        String response = mockMvc.perform(post("/api/expenses").contentType("application/json")
                        .content(objectMapper.writeValueAsString(
                                expensePayload("Temp expense", "50.00", "TempCategory", "2026-07-07"))))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(delete("/api/expenses/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/expenses/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteExpense_unknownId_returns404() throws Exception {
        mockMvc.perform(delete("/api/expenses/{id}", 999999L))
                .andExpect(status().isNotFound());
    }

    @Test
    void getTotal_withCategory_returnsCategoryTotal() throws Exception {
        mockMvc.perform(post("/api/expenses").contentType("application/json")
                .content(objectMapper.writeValueAsString(
                        expensePayload("Groceries", "300.00", "TotalTestCategory", "2026-07-08"))));
        mockMvc.perform(post("/api/expenses").contentType("application/json")
                .content(objectMapper.writeValueAsString(
                        expensePayload("Snacks", "50.00", "TotalTestCategory", "2026-07-09"))));

        mockMvc.perform(get("/api/expenses/total").param("category", "TotalTestCategory"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category", is("TotalTestCategory")))
                .andExpect(jsonPath("$.total", is(350.00)));
    }
}
