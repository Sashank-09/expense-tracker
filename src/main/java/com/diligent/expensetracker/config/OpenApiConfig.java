package com.diligent.expensetracker.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI expenseTrackerAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Smart Expense Tracker REST API")
                        .description("""
                                REST API developed as part of the
                                Diligent Software Engineering Apprenticeship Assignment.

                                Features:
                                • Add Expense
                                • Retrieve Expenses
                                • Filter by Category
                                • Calculate Total Expenses
                                • Delete Expenses
                                """)
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Enukurthi Sashank")
                                .email("your-email@example.com")));
    }
}