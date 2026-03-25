# AGENTS.md - HireDev Project Guidelines

This document provides guidance for AI coding agents working on the HireDev codebase, a Spring Cloud microservice online recruitment system.

## Project Overview

- **Language:** Java 8
- **Framework:** Spring Boot 2.6.11 + Spring Cloud 2021.0.4 + Spring Cloud Alibaba 2021.0.1.0
- **ORM:** MyBatis Plus 3.5.0
- **Build Tool:** Maven (multi-module)
- **Base Package:** `com.power`

## Module Structure

```
hire-dev (parent POM)
  hire-common         - Shared utilities, enums, exception handling, response wrappers
  hire-pojo           - Entity classes (depends on hire-common)
  hire-api            - Shared web API interfaces (depends on hire-pojo)
  service-user-7001   - User microservice (port 7001)
  service-company-6001- Company microservice (port 6001)
  service-work-3001   - Work/job microservice (port 3001)
  service-file-5001   - File microservice (port 5001)
  service-resource-4001- Resource microservice (port 4001)
  auth-service-8111   - Authentication service (port 8111)
  gateway-8000        - API gateway (port 8000)
```

Dependency chain: `hire-common` <- `hire-pojo` <- `hire-api` <- all service modules.

## Build & Run Commands

```bash
# Build entire project
mvn clean install

# Build a single module (from project root)
mvn clean install -pl service-user-7001 -am

# Run a specific service (from module directory)
mvn spring-boot:run

# Package without tests
mvn clean package -DskipTests

# Compile only (no packaging)
mvn compile
```

**No tests exist in this project.** There are no `src/test` directories in any module. Do not create test commands or expect tests to pass.

## Code Style Guidelines

### Imports

- Use single class imports, never wildcard `*` imports
- Import order: `java.*` -> `javax.*` -> third-party (`com.baomidou.*`, `org.springframework.*`) -> project (`com.power.*`)
- No blank lines between import groups (current codebase does not enforce strict grouping)

### Naming Conventions

| Element | Convention | Example |
|---------|-----------|---------|
| Classes | PascalCase | `Users`, `ChatMessage`, `ResumeProjectExp` |
| Interfaces | PascalCase | `StuService` |
| Implementations | `{Name}Impl` in `impl` package | `StuServiceImpl` |
| Methods | camelCase | `saveUser()`, `findById()` |
| Fields | camelCase | `userId`, `createdTime` |
| Constants/Enums | UPPER_SNAKE_CASE | `SUCCESS`, `UN_LOGIN` |
| DB tables | snake_case | `chat_message`, `resume_project_exp` |
| DB columns | snake_case | `hr_id`, `company_id` |
| Mapper interfaces | `{Entity}Mapper` | `StuMapper` |
| Controllers | `{Entity}Controller` | `HelloController` |

### Package Conventions

```
com.power.pojo        - Entity classes
com.power.controller  - REST controllers
com.power.service     - Service interfaces
com.power.service.impl- Service implementations
com.power.mapper      - MyBatis mapper interfaces
com.power.result      - Response wrapper classes
com.power.utils       - Utility classes
com.power.enums       - Enumerations
com.power.exceptions  - Custom exceptions
```

### Entity/POJO Conventions

- All entities implement `Serializable` with `serialVersionUID = 1L`
- Write manual getters, setters, and `toString()` (project convention; Lombok `@Data` is NOT used on most entities despite being a dependency)
- Javadoc block with `@author power` and `@since YYYY-MM-DD` on class level
- Use `LocalDateTime` for timestamps, `LocalDate` for dates
- Primary key annotation: `@TableId(value = "id", type = IdType.ASSIGN_ID)` (snowflake algorithm)

### Annotations

- `@SpringBootApplication` on main class named `MainApplication`
- `@EnableDiscoveryClient` for Nacos registration
- `@MapperScan(basePackages = "com.power.mapper")` on main application class
- `@RestController` + `@RequestMapping` on controllers
- `@Service` on service implementations
- `@Transactional` on service methods requiring transactions
- `@Autowired` for dependency injection (field injection pattern)
- `@Slf4j` for logging (only when needed)

### Response Format

Use `GraceJsonResult` (preferred) or `JsonResult` for API responses:

```java
// Success
return GraceJsonResult.ok();
return GraceJsonResult.ok(data);

// Error with enum
return GraceJsonResult.errorCustom(ResponseStatusEnum.UN_LOGIN);

// Error with message
return GraceJsonResult.errorMsg("Error message");
```

Use `ResponseStatusEnum` for typed status codes. Do not hardcode status integers.

### Error Handling

- Use `ResponseStatusEnum` for all custom status codes (defined in hire-common)
- Return `GraceJsonResult` wrappers from controllers, not raw exceptions
- Use `@Transactional` on service methods that modify data

### Layer Responsibilities

- **Controller**: Receives HTTP requests, delegates to service, returns `GraceJsonResult`
- **Service (interface + impl)**: Business logic, transactional boundaries
- **Mapper (extends BaseMapper<T>)**: Data access via MyBatis Plus; custom SQL in XML files under `resources/mapper/`

## Configuration

- Main config: `application.yml` (profile selection, MyBatis Plus settings)
- Profile configs: `application-dev.yml`, `application-prod.yml`
- Each service module must have datasource config in its profile yml:
  ```yaml
  spring:
    datasource:
      type: com.zaxxer.hikari.HikariDataSource
      driver-class-name: com.mysql.cj.jdbc.Driver
      url: jdbc:mysql://localhost:3306/hire-dev?...
      username: root
      password: 123456
  ```

## Infrastructure Dependencies

- **MySQL 8.x**: Primary database (`hire-dev` schema)
- **Nacos**: Service discovery at `localhost:8848` (run via Docker)
- **MinIO**: File storage (service-file-5001)

## Important Notes

- Comments and Javadoc are written in Simplified Chinese
- SQL schema is in `sql/hire-dev.sql` (20 tables, InnoDB, utf8mb4)
- No code formatter, checkstyle, or static analysis tools are configured
- Some modules (auth-service-8111, gateway-8000, service-work-3001, service-file-5001, service-resource-4001) have no source code yet
- When adding new code, follow existing patterns in `service-user-7001` or `service-company-6001`
