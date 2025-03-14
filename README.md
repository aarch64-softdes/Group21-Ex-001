# Ex01 - Student Management System

## Source code structure

```
.
├── backend
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java/com/tkpm/sms
│       │   │               ├── controller
│       │   │               │   ├── MetadataController.java
│       │   │               │   └── StudentController.java
│       │   │               ├── dto
│       │   │               │   ├── StudentRequest.java
│       │   │               │   └── StudentResponse.java
│       │   │               ├── entity
│       │   │               │   └── Student.java
│       │   │               ├── enums
│       │   │               │   ├── EnumUtils.java
│       │   │               │   ├── Faculty.java
│       │   │               │   ├── Gender.java
│       │   │               │   └── Status.java
│       │   │               ├── exceptions
│       │   │               │   └── GlobalExceptionHandler.java
│       │   │               ├── repository
│       │   │               │   └── StudentRepository.java
│       │   │               ├── service
│       │   │               │   └── StudentService.java
│       │   │               └── SmsApplication.java
│       │   └── resources
│       │       └── application.properties
│       └── test/java/com/tkpm/sms/SmsApplicationTests.java
├── database_creation.sql
├── frontend
│   ├── components.json
│   ├── eslint.config.js
│   ├── index.html
│   ├── package.json
│   ├── package-lock.json
│   ├── public
│   │   └── vite.svg
│   ├── README.md
│   ├── src
│   │   ├── App.tsx
│   │   ├── assets
│   │   │   └── react.svg
│   │   ├── components
│   │   │   ├── filter
│   │   │   │   └── SearchFilter.tsx
│   │   │   ├── student
│   │   │   │   ├── StudentDetail.tsx
│   │   │   │   └── StudentForm.tsx
│   │   │   ├── table
│   │   │   │   ├── ActionCell.tsx
│   │   │   │   ├── GenericTable.tsx
│   │   │   │   ├── SkeletonTable.tsx
│   │   │   │   ├── TablePagination.tsx
│   │   │   │   └── TableSort.tsx
│   │   │   └── ui/...
│   │   ├── hooks
│   │   │   ├── useDebounce.ts
│   │   │   ├── useMetadata.ts
│   │   │   ├── useStudentApi.ts
│   │   │   └── useTableDataOperation.ts
│   │   ├── index.css
│   │   ├── lib
│   │   │   └── utils.ts
│   │   ├── main.tsx
│   │   ├── pages
│   │   │   └── studentPage.tsx
│   │   ├── services
│   │   │   ├── metadataService.ts
│   │   │   └── studentService.ts
│   │   ├── types
│   │   │   ├── apiResponse.ts
│   │   │   ├── filter.ts
│   │   │   ├── student.ts
│   │   │   └── table.ts
│   │   └── vite-env.d.ts
│   ├── tailwind.config.js
│   ├── tsconfig.app.json
│   ├── tsconfig.json
│   ├── tsconfig.node.json
│   └── vite.config.ts
└── README.md

35 directories, 73 files

```

## Dependencies

- Node.js v20.0.0
- npm v7.24.0
- OpenJDK 21
- Maven 3.8.4
- PostgreSQL 12.0+

## Installation

1. Install the dependencies.

2. Clone the repository

```bash
git clone https://github.com/aarch64-softdes/Group21-Ex-001
```

3. Run the following commands:

- **Frontend**

```bash
cd frontend
npm install
npm run dev
```

- **Backend**
  - Ensure that the PostgreSQL server is running.
  - Create a database named `sms`.
  - Run the `database_creation.sql` script to create the tables.
  - Run the following command:
    ```bash
    cd backend
    mvn spring-boot:run
    ```
