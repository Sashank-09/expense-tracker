# AI_NOTES.md

# AI Usage Notes

I used AI tools (primarily Claude and ChatGPT) as development assistants throughout this assignment. AI helped accelerate implementation, documentation, debugging, and code review, while I remained responsible for understanding, validating, testing, and refining the final solution.

Every significant implementation decision in the submitted project was manually reviewed before being accepted. The final repository reflects my understanding of Spring Boot, REST API development, validation, exception handling, testing, and software engineering best practices rather than accepting AI-generated output without verification.

---

# 1. AI-generated vs. implemented by me

## AI-assisted

AI assisted with generating or suggesting the initial versions of:

- Spring Boot project structure.
- Expense model.
- ExpenseRequest and TotalResponse DTOs.
- ExpenseService implementation.
- ExpenseController implementation.
- GlobalExceptionHandler and custom exception classes.
- Initial JUnit and MockMvc test classes.
- Validation annotations for request DTOs.
- Initial README and AI_NOTES documentation.

AI also assisted with improving the project by suggesting:

- Swagger/OpenAPI integration.
- OpenAPI schema annotations for DTOs and models.
- Structured API error responses.
- Structured logging using SLF4J.
- Better exception handling patterns.
- Documentation improvements.
- Design discussions and engineering trade-offs throughout development.

---

## Manual review and implementation

Although AI generated initial implementations and suggestions, I personally:

- Reviewed every generated class before using it.
- Modified AI-generated code wherever necessary.
- Verified every REST endpoint matched the assignment requirements.
- Reviewed request and response models for correctness.
- Verified validation rules and error handling.
- Improved exception handling using a reusable API error model.
- Integrated Swagger/OpenAPI documentation.
- Implemented structured logging using SLF4J.
- Refined project documentation and README.
- Reviewed naming conventions, package organization, and overall code readability.
- Ensured the project followed standard Spring Boot and Maven conventions.
- Verified every AI-generated suggestion before deciding whether it should be included in the final solution.

---

# 2. What I validated, tested, and changed

Rather than assuming AI-generated code was correct, I validated every important implementation decision before including it in the project.

## Monetary calculations

AI initially suggested using standard numeric types for expense amounts.

I chose to use `BigDecimal` because monetary calculations should avoid floating-point precision errors.

---

## Thread safety

The service stores expenses using `ConcurrentHashMap` together with `AtomicLong`.

I verified this approach provides safe concurrent access while keeping the implementation simple and aligned with the assignment requirement of using in-memory storage.

---

## Bean Validation

I tested validation for:

- Missing title
- Blank category
- Negative amount
- Zero amount
- Missing date
- Invalid request payloads

I verified that invalid requests consistently returned structured HTTP 400 responses with detailed validation errors.

---

## Exception handling

I reviewed and refined the AI-generated exception handling by introducing:

- A reusable `ApiError` response model.
- Field-level validation errors.
- Appropriate HTTP status codes.
- Consistent JSON error responses across the application.

I verified handling for:

- Validation failures
- Expense not found
- Unexpected server exceptions

---

## REST endpoints

I manually tested every endpoint using both Postman and Swagger UI.

Verified operations include:

- Add Expense
- View All Expenses
- Get Expense by ID
- Filter Expenses by Category
- Calculate Overall Total
- Calculate Category Total
- Delete Expense

I also verified invalid IDs and invalid requests returned the expected responses.

---

## API documentation

After integrating Swagger/OpenAPI, I verified that:

- Swagger UI loaded successfully.
- Every endpoint was documented.
- Request and response schemas rendered correctly.
- DTO descriptions appeared correctly.
- Interactive testing worked for every endpoint.

---

## Structured logging

I verified that:

- Expense creation logs are written at INFO level.
- Retrieval operations generate DEBUG logs.
- Invalid requests and missing resources generate WARN logs.
- Unexpected exceptions generate ERROR logs.

---

## Automated testing

After implementing new features or modifying AI-generated code, I reran the Maven test suite and manually tested the API to ensure new changes did not introduce regressions.

---

## Clean checkout validation

On 1 Aug 2026, I cloned the repository fresh into a separate directory
and ran `mvn clean install` followed by `mvn test` and `mvn spring-boot:run`
against the clone (not my working copy) to confirm the README's commands
work exactly as written for someone starting from scratch. All 18 tests
passed and the server started cleanly on port 8081.

---

# 3. AI suggestions I decided not to use

During development, AI suggested several additional improvements.

## Database persistence

AI suggested replacing the in-memory storage with a relational database.

I intentionally kept the application in-memory because the assignment explicitly stated that a database was not required. This kept the implementation focused on the required functionality.

---

## Repository layer

AI suggested introducing repository interfaces and additional abstraction layers.

Since the project stores data only in memory, I decided that introducing repository abstractions would add complexity without providing practical benefits.

---

## Authentication and authorization

AI suggested implementing JWT authentication.

I intentionally chose not to implement authentication because it was outside the assignment scope and would distract from the required functionality.

---

## Additional bonus features

AI suggested adding Docker support and several additional enhancements.

I intentionally selected only Swagger/OpenAPI because the assignment specifies choosing at most one optional bonus feature. This kept the project focused while still demonstrating additional engineering effort.

---

# 4. Engineering decisions

AI frequently suggested multiple implementation approaches rather than a single solution.

For each significant decision, I evaluated the available options before selecting the implementation that best matched the assignment requirements.

Examples include:

- Using `ConcurrentHashMap` and `AtomicLong` for thread-safe in-memory storage.
- Using `BigDecimal` for monetary values.
- Keeping the application database-free.
- Choosing Swagger/OpenAPI as the single optional bonus feature.
- Using structured exception handling instead of returning generic error responses.
- Following the standard Spring Boot and Maven project structure.

---

# 5. Development workflow

My development workflow was:

1. Read and understand the assignment requirements.
2. Use AI to generate an initial implementation.
3. Review every generated class.
4. Modify generated code where necessary.
5. Validate important implementation decisions.
6. Execute the application locally.
7. Test every REST endpoint manually using Postman and Swagger UI.
8. Run the automated Maven test suite.
9. Improve documentation and project structure.
10. Perform a final review before submission.

AI significantly accelerated development, but every important implementation decision was manually reviewed, validated, and tested before becoming part of the final project.

---

# 6. Reflection

This assignment reinforced that AI is most valuable as a software engineering assistant rather than a replacement for engineering judgment.

AI accelerated implementation, documentation, debugging, and code review, while manual validation, testing, and decision-making ensured the final solution remained correct, maintainable, and aligned with the assignment requirements.

The most valuable aspect of using AI was not generating code quickly, but critically evaluating its suggestions, understanding the implementation, and making informed engineering decisions before accepting them into the final solution.