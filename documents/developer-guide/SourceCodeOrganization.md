# Source Code Organization

The repository is split into a Spring Boot backend and a React frontend. All developer documentation lives under `documents/`.

## Root Layout

```
.
├── backend/
├── frontend/
├── documents/
├── database_creation.sql
├── dirtree.txt
├── Jenkinsfile
```

### Backend

The `backend/` module follows Clean Architecture conventions and is built with Maven.

```
backend/
├── docker-compose.yml           # Local PostgreSQL container
├── mvnw, mvnw.cmd, pom.xml      # Maven wrapper and build config
├── logs/                        # Development log files
└── src
    ├── main
    │   ├── java/com/tkpm/sms/
    │   │   ├── application/      # DTOs, services, validators
    │   │   ├── domain/           # Entities, value objects, repository interfaces
    │   │   ├── infrastructure/   # Spring configs and persistence adapters
    │   │   ├── application/validator/  # Cross-field validators
    │   │   └── presentation/     # REST controllers and error handlers
    │   └── resources/            # application.yml, i18n, templates, migrations
    └── test/java                # Unit tests mirroring main packages
```
key Backend concepts
* **Clean Architecture**: Domain-centric design with clear boundaries between layers
* **Feature-based Organization**: Each feature has its own sub-package with matching service, repository, and controller classes
* **Dependency Injection**: Relies on Spring Beans for inversion of control
* **Domain-Driven Design**: Rich domain model with business logic encapsulation

### Frontend

The React client resides in `frontend/` and is built with Vite.

```
frontend/
├── src/
│   ├── core/                    # API configuration and shared types
│   ├── features/                # Feature pages, services and components
│   ├── shared/                  # Reusable UI pieces and hooks
│   ├── assets/                  # Static images
│   └── main.tsx                 # Application entry point
├── public/                      # Files served as-is
├── tailwind.config.js
├── vite.config.ts
└── package.json
```
Frontend Architecture
* **Component-Based Design**: Functional React components with hooks
* **Feature-First Organization**: Code organized by domain features
* **TypeScript**: Strong typing for improved maintainability
* **React Query**: For API data fetching and client-side caching

### Documentation and Utilities

- `documents/developer-guide/` – reference documentation for contributors
- `database_creation.sql` – SQL script for bootstrapping a new database
- `dirtree.txt` – the full directory listing used by this guide

Each module keeps tests next to the code they cover, making it easy to locate related files. Following this structure helps new developers quickly navigate the project.

Every feature resides in its own sub-package with matching service, repository and controller classes. For example `com.tkpm.sms.course` includes `CourseService`, `CourseRepository` and `CourseController`. This vertical slice approach keeps code for a feature in one place.

