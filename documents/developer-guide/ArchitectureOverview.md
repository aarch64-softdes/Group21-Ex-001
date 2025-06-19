# Architecture Overview

This project follows the principles of **Clean Architecture** with distinct layers separating framework concerns from domain logic. The goal is to ensure that business rules remain independent of the surrounding infrastructure while still leveraging Spring Boot and React for rapid development.
Dependency injection in the backend is provided by Spring's Bean container. Services and repositories are registered as beans and injected via constructors.




```mermaid
graph TD
    browser[Browser]
    react[React Frontend]
    controller[Spring MVC Controllers]
    service[Application Services]
    domain[Domain Model]
    repo[Repository Interfaces]
    db[(PostgreSQL)]

    browser -->|HTTP| react
    react -->|REST| controller
    controller --> service
    service --> domain
    domain --> repo
    repo --> db
```

## Layer Responsibilities

### Presentation
- Package `com.tkpm.sms.presentation`.
- Exposes REST endpoints under `/api` using Spring MVC.
- Handles global exception translation to JSON responses.
- Interceptors such as `ContentLanguageInterceptor` set the locale per request.

- Endpoints are currently open with no authentication or authorization.
### Application
- Package `com.tkpm.sms.application`.
- Coordinates use cases by orchestrating domain services and repositories.
- Maps request DTOs to domain objects using MapStruct mappers.
- Executes use cases within transactional boundaries.
- Contains validators and transaction boundaries.

### Domain
- Package `com.tkpm.sms.domain`.
- Contains the core entities (e.g., `Student`, `Course`) and value objects.
- Defines repository interfaces and domain services containing business rules.
- Domain objects are persistence-agnostic and do not depend on Spring.

### Infrastructure
- Package `com.tkpm.sms.infrastructure`.
- Provides configuration, persistence adapters, logging and interceptors.
- Repository implementations use Spring Data JPA to interact with PostgreSQL.
- Additional modules handle caching, message translation and integration with Elasticsearch.

### Frontend
- The React application under `frontend/` communicates with the backend using the REST API.
- React components handle form validation, routing and internationalization using i18n files.

## Data Flow
1. **Request** – A browser sends an HTTP request to the React frontend.
2. **API Call** – React issues a REST call to the backend controller.
3. **Application Logic** – Controllers convert the input to DTOs and delegate to services.
4. **Domain Logic** – Services perform validations, execute domain rules and persist aggregates via repositories.
5. **Persistence** – Repository implementations interact with PostgreSQL using JPA.
6. **Response** – Results are mapped back to DTOs, returned through controllers and delivered to the frontend.

## Cross-Cutting Concerns
- **Logging** – Slf4j with optional Elasticsearch integration. Logback appenders write JSON files under `backend/logs` which can be shipped to the ELK stack.
- **Localization** – `TranslatorService` resolves messages from i18n resource bundles. The `ContentLanguageInterceptor` reads the `Content-Language` header and stores the locale in a `ThreadLocal` for the duration of the request.
- **Error Handling** – A global `@ControllerAdvice` converts exceptions to `ApplicationResponseDto` objects for consistent JSON responses.

- **Caching** – Frequently used lookups such as faculties or programs are cached with Spring's `@Cacheable` abstraction backed by Caffeine. Cache keys include the current locale when the data contains translations.
## Build and Testing
- Maven builds the backend; Node and Vite build the frontend.
- Unit tests reside under `backend/src/test/java` and use JUnit 5.
- Frontend tests can be added with Jest.

This layered approach keeps the domain model isolated and allows the system to evolve without coupling business rules to specific technologies.

## Deployment Environments
Each layer can be deployed separately if required. The backend produces a runnable Spring Boot JAR while the frontend compiles to static assets served by a web server or CDN. CI pipelines run linting and tests before packaging artifacts.

## Modularization Strategy
Submodules within `backend/` break down features such as student management, enrollment, and notifications. Shared contracts and utilities reside in `backend/common`. This modular approach keeps features isolated and easy to maintain.

### Core Modules
The main modules included with the system are:

- **student** – CRUD operations and validation rules for student records
- **course** – subject catalog, scheduling and enrolment management
- **settings** – runtime configuration loaded from the database
- **translations** – i18n resource loading and language management

Each module exposes its own services and controllers while depending only on domain interfaces. Infrastructure components like JPA repositories live in a parallel package under the same module name.

```mermaid
graph LR
    subgraph student
        A(Service) --> B(Repository)
    end
    subgraph course
        C(Service) --> D(Repository)
    end
    subgraph settings
        E(Service) --> F(Repository)
    end
    E ---|calls| A
    C ---|uses| E
```

## Example Request Flow
```mermaid
sequenceDiagram
    participant B as Browser
    participant F as Frontend
    participant C as Controller
    participant S as Service
    participant R as Repository
    participant DB as Database

    B->>F: Submit registration form
    F->>C: POST /api/students
    C->>S: createStudent(dto)
    S->>R: save(entity)
    R->>DB: INSERT student
    DB-->>R: id
    R-->>S: entity with id
    S-->>C: StudentDto
    C-->>F: 201 Created
```

These additional diagrams and explanations should make the high-level design easier to understand.

## Configuration and Environment Separation
Spring profiles allow different configuration for development and production. Properties under `src/main/resources/application-dev.yml` are loaded when `spring.profiles.active=dev`. Deployment pipelines supply `application-prod.yml` which connects to managed services such as PostgreSQL and Elasticsearch.

Settings stored in the database override values defined in YAML. The `SettingService` checks the database first and falls back to configuration files if a record is missing.

## Internationalization Workflow
Text displayed in the UI is stored in the `text_contents` and `translations` tables. When a translation for the current locale is missing, the application falls back to the default `is_original` text. Developers can add new languages by seeding translations and including message bundles in `src/main/resources/i18n`.

