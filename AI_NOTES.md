# AI_NOTES.md

# AI Usage Notes

I used AI (primarily Claude and ChatGPT) as a development assistant throughout this assignment. AI helped accelerate implementation and documentation, but I reviewed, tested, and validated every part of the final submission before considering it complete.

The final repository reflects my understanding of Spring Boot, REST API development, validation, exception handling, and testing rather than accepting AI-generated output without verification.

---

## 1. AI-generated vs. implemented by me

### AI-assisted

AI helped with:

- Creating the initial Spring Boot project structure.
- Generating the first version of the model, DTOs, service, controller, and exception classes.
- Suggesting validation annotations for request DTOs.
- Generating an initial set of JUnit and MockMvc tests.
- Drafting the README and this AI_NOTES document.

AI also assisted with:

- Improving the API using Swagger/OpenAPI documentation.
- Adding OpenAPI schema annotations to DTOs and models.
- Designing a structured API error response.
- Implementing structured logging using SLF4J.
- Reviewing the README for completeness and clarity.
- Discussing engineering trade-offs and validating design decisions throughout development.

### My contribution

I personally:

- Reviewed every generated class before using it.
- Verified the REST endpoints matched the assignment requirements.
- Checked request and response models for correctness.
- Confirmed that validation behaved correctly for invalid inputs.
- Verified exception handling produced the expected HTTP status codes.
- Executed the application locally and manually tested every endpoint.
- Ran the automated test suite before finalizing the project.
- Reviewed naming, package organization, and code readability to ensure the project followed common Spring Boot conventions.

I also:

- Added Swagger/OpenAPI documentation.
- Implemented structured logging using SLF4J.
- Refined exception handling into a reusable API error model.
- Improved project documentation and README.
- Reviewed every AI-generated suggestion before deciding whether it aligned with the assignment requirements.

---

## 2. What I validated and why

Rather than assuming AI-generated code was correct, I validated each important design decision.

### Monetary calculations

The project uses `BigDecimal` instead of `double` for expense amounts.

I kept this implementation because financial calculations should avoid floating-point precision issues.

---

### Thread safety

The service stores expenses using `ConcurrentHashMap` together with `AtomicLong`.

Since Spring Boot processes multiple requests concurrently, I verified that this approach safely supports concurrent access without introducing synchronization issues.

---

### Bean Validation

I tested validation for:

- Missing title
- Blank category
- Negative amount
- Zero amount
- Invalid request payloads

and confirmed the API returned appropriate HTTP 400 responses.

---

### REST endpoints

I manually tested:

- Add Expense
- View All Expenses
- Filter by Category
- Calculate Total Expenses
- Calculate Category Total
- Delete Expense

using both Postman and Swagger UI after integrating OpenAPI documentation.

I also verified that deleting a non-existent expense returns the appropriate error response.

---

### Structured Logging

I verified that:

- Expense creation is logged at INFO level.
- Retrieval operations generate DEBUG logs.
- Missing resources generate WARN logs.
- Unexpected exceptions generate ERROR logs with stack traces.

---

### API Documentation

I verified that:

- Swagger UI loads successfully.
- Every endpoint is documented.
- DTOs display descriptions and example values.
- Request and response models render correctly.

---

### Automated tests

Before finalizing the submission, I executed the project's test suite using Maven to ensure all tests passed successfully.

---

## 3. AI suggestions I decided not to use

AI suggested several additional improvements during development.

### Database persistence

One suggestion was replacing the in-memory store with a database.

I intentionally kept in-memory storage because the assignment explicitly states that a database is not required. Keeping the implementation simple allowed me to focus on correctness and code quality.

---

### Additional architectural layers

AI suggested introducing repository interfaces and additional abstraction layers.

Since the application stores data only in memory, I decided that extra layers would increase complexity without improving the solution.

---

### Authentication

JWT authentication was also suggested.

I chose not to implement authentication because it was outside the assignment scope and would have distracted from the required functionality.

---

## 4. My development workflow

My development process was:

1. Understand the assignment requirements.
2. Use AI to generate an initial implementation.
3. Review every generated class.
4. Modify code where necessary.
5. Run the application locally.
6. Test every endpoint manually.
7. Execute the automated test suite.
8. Refine the documentation before submission.

AI significantly accelerated development, but every important implementation decision was reviewed and validated before being included in the final project.

---

## 5. Reflection

This assignment reinforced that AI is most valuable as a productivity tool rather than a replacement for engineering judgment. AI accelerated implementation, documentation, and code review, while manual validation, testing, and design decisions ensured the final solution remained correct, maintainable, and aligned with the assignment requirements.