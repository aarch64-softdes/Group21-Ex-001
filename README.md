# Ex01 - Student Management System

## Source code structure

```
.
├── backend
│   ├── data
│   ├── docker-compose.yml
│   ├── logs
│   │   ├── application.json
│   │   ├── application.log
│   │   └── archived
│   ├── mvnw
│   ├── mvnw.cmd
│   ├── pom.xml
│   └── src
│       ├── main
│       │   ├── java
│       │   │   └── com
│       │   │       └── tkpm
│       │   │           └── sms
│       │   │               ├── aspect
│       │   │               │   └── ControllerLoggingAspect.java
│       │   │               ├── config
│       │   │               │   ├── ElasticsearchConfig.java
│       │   │               │   └── FileMapperConfig.java
│       │   │               ├── controller
│       │   │               │   ├── FacultyController.java
│       │   │               │   ├── FileController.java
│       │   │               │   ├── MetadataController.java
│       │   │               │   ├── ProgramController.java
│       │   │               │   ├── StatusController.java
│       │   │               │   ├── StudentController.java
│       │   │               │   └── TestController.java
│       │   │               ├── dto
│       │   │               │   ├── request
│       │   │               │   │   ├── AddressCreateRequestDto.java
│       │   │               │   │   ├── AddressUpdateRequestDto.java
│       │   │               │   │   ├── common
│       │   │               │   │   │   └── BaseCollectionRequest.java
│       │   │               │   │   ├── FacultyRequestDto.java
│       │   │               │   │   ├── IdentityCreateRequestDto.java
│       │   │               │   │   ├── IdentityUpdateRequestDto.java
│       │   │               │   │   ├── ProgramRequestDto.java
│       │   │               │   │   ├── StatusRequestDto.java
│       │   │               │   │   ├── StudentCollectionRequest.java
│       │   │               │   │   ├── StudentCreateRequestDto.java
│       │   │               │   │   └── StudentUpdateRequestDto.java
│       │   │               │   └── response
│       │   │               │       ├── common
│       │   │               │       │   ├── ApplicationResponseDto.java
│       │   │               │       │   ├── ListResponse.java
│       │   │               │       │   └── PageDto.java
│       │   │               │       ├── FacultyDto.java
│       │   │               │       ├── identity
│       │   │               │       │   └── IdentityDto.java
│       │   │               │       ├── ProgramDto.java
│       │   │               │       ├── StatusDto.java
│       │   │               │       └── student
│       │   │               │           ├── StudentDto.java
│       │   │               │           ├── StudentFileDto.java
│       │   │               │           └── StudentMinimalDto.java
│       │   │               ├── entity
│       │   │               │   ├── Address.java
│       │   │               │   ├── Faculty.java
│       │   │               │   ├── Identity.java
│       │   │               │   ├── Program.java
│       │   │               │   ├── Status.java
│       │   │               │   └── Student.java
│       │   │               ├── enums
│       │   │               │   ├── Faculty.java
│       │   │               │   ├── Gender.java
│       │   │               │   ├── IdentityType.java
│       │   │               │   ├── LoggerType.java
│       │   │               │   ├── Status.java
│       │   │               │   └── SupportedFileType.java
│       │   │               ├── exceptions
│       │   │               │   ├── ApplicationException.java
│       │   │               │   ├── ErrorCode.java
│       │   │               │   └── GlobalExceptionHandler.java
│       │   │               ├── factories
│       │   │               │   └── FileStrategyFactory.java
│       │   │               ├── logging
│       │   │               │   ├── BaseLogger.java
│       │   │               │   ├── LogEntry.java
│       │   │               │   ├── logger
│       │   │               │   │   ├── AbstractLogger.java
│       │   │               │   │   ├── ConsoleLogger.java
│       │   │               │   │   ├── ElasticsearchLogger.java
│       │   │               │   │   ├── FileLogger.java
│       │   │               │   │   └── JsonLogger.java
│       │   │               │   ├── LoggerManager.java
│       │   │               │   └── metadata
│       │   │               │       ├── BaseMetadata.java
│       │   │               │       ├── ExceptionMetadata.java
│       │   │               │       ├── RequestMetadata.java
│       │   │               │       └── ResponseMetadata.java
│       │   │               ├── mapper
│       │   │               │   ├── AddressMapper.java
│       │   │               │   ├── FacultyMapper.java
│       │   │               │   ├── IdentityMapper.java
│       │   │               │   ├── ProgramMapper.java
│       │   │               │   ├── StatusMapper.java
│       │   │               │   └── StudentMapper.java
│       │   │               ├── repository
│       │   │               │   ├── AddressRepository.java
│       │   │               │   ├── FacultyRepository.java
│       │   │               │   ├── IdentityRepository.java
│       │   │               │   ├── ProgramRepository.java
│       │   │               │   ├── StatusRepository.java
│       │   │               │   └── StudentRepository.java
│       │   │               ├── service
│       │   │               │   ├── AddressService.java
│       │   │               │   ├── FacultyService.java
│       │   │               │   ├── FileService.java
│       │   │               │   ├── IdentityService.java
│       │   │               │   ├── ProgramService.java
│       │   │               │   ├── StatusService.java
│       │   │               │   └── StudentService.java
│       │   │               ├── SmsApplication.java
│       │   │               ├── specification
│       │   │               │   └── StudentSpecifications.java
│       │   │               ├── strategies
│       │   │               │   ├── CsvStrategy.java
│       │   │               │   ├── FileStrategy.java
│       │   │               │   └── JsonStrategy.java
│       │   │               ├── utils
│       │   │               │   ├── EnumUtils.java
│       │   │               │   ├── ImportFileUtils.java
│       │   │               │   └── JsonUtils.java
│       │   │               └── validator
│       │   │                   ├── identity
│       │   │                   │   ├── IdentityConstraint.java
│       │   │                   │   └── IdentityValidator.java
│       │   │                   └── status
│       │   │                       ├── StatusConstraint.java
│       │   │                       └── StatusValidator.java
│       │   └── resources
│       │       ├── application-dev.yml
│       │       ├── application.sample.yml
│       │       ├── application.yml
│       │       ├── db
│       │       │   └── migration
│       │       │       └── initial-schema.sql
│       │       ├── logback.xml
│       │       ├── static
│       │       └── templates
│       └── test
│           └── java
│               └── com
│                   └── tkpm
│                       └── sms
│                           └── SmsApplicationTests.java
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
│   │   │   ├── faculty
│   │   │   │   ├── FacultyDetail.tsx
│   │   │   │   └── FacultyForm.tsx
│   │   │   ├── filter
│   │   │   │   └── SearchFilter.tsx
│   │   │   ├── layouts
│   │   │   │   ├── AppSidebar.tsx
│   │   │   │   └── Layout.tsx
│   │   │   ├── program
│   │   │   │   ├── ProgramDetail.tsx
│   │   │   │   └── ProgramForm.tsx
│   │   │   ├── status
│   │   │   │   ├── StatusDetail.tsx
│   │   │   │   └── StatusForm.tsx
│   │   │   ├── student
│   │   │   │   ├── AddressForm.tsx
│   │   │   │   ├── StudentDetail.tsx
│   │   │   │   └── StudentForm.tsx
│   │   │   ├── table
│   │   │   │   ├── ActionCell.tsx
│   │   │   │   ├── GenericTable.tsx
│   │   │   │   ├── SkeletonTable.tsx
│   │   │   │   ├── TablePagination.tsx
│   │   │   │   └── TableSort.tsx
│   │   │   └── ui
│   │   │       ├── accordion.tsx
│   │   │       ├── alert-dialog.tsx
│   │   │       ├── avatar.tsx
│   │   │       ├── badge.tsx
│   │   │       ├── button.tsx
│   │   │       ├── card.tsx
│   │   │       ├── checkbox.tsx
│   │   │       ├── dialog.tsx
│   │   │       ├── dropdown-menu.tsx
│   │   │       ├── form.tsx
│   │   │       ├── input.tsx
│   │   │       ├── label.tsx
│   │   │       ├── loadingButton.tsx
│   │   │       ├── popover.tsx
│   │   │       ├── select.tsx
│   │   │       ├── separator.tsx
│   │   │       ├── sheet.tsx
│   │   │       ├── sidebar.tsx
│   │   │       ├── skeleton.tsx
│   │   │       ├── table.tsx
│   │   │       └── tooltip.tsx
│   │   ├── hooks
│   │   │   ├── useDebounce.ts
│   │   │   ├── useFacultyApi.ts
│   │   │   ├── useMetadata.ts
│   │   │   ├── use-mobile.ts
│   │   │   ├── useProgramApi.ts
│   │   │   ├── useStatusApi.ts
│   │   │   ├── useStudentApi.ts
│   │   │   └── useTableDataOperation.ts
│   │   ├── index.css
│   │   ├── lib
│   │   │   └── utils.ts
│   │   ├── main.tsx
│   │   ├── pages
│   │   │   ├── facultyPage.tsx
│   │   │   ├── programPage.tsx
│   │   │   ├── statusPage.tsx
│   │   │   └── studentPage.tsx
│   │   ├── services
│   │   │   ├── facultyService.ts
│   │   │   ├── metadataService.ts
│   │   │   ├── programService.ts
│   │   │   ├── statusService.ts
│   │   │   └── studentService.ts
│   │   ├── types
│   │   │   ├── address.ts
│   │   │   ├── apiResponse.ts
│   │   │   ├── faculty.ts
│   │   │   ├── filter.ts
│   │   │   ├── identityDocument.ts
│   │   │   ├── program.ts
│   │   │   ├── status.ts
│   │   │   ├── student.ts
│   │   │   └── table.ts
│   │   └── vite-env.d.ts
│   ├── tailwind.config.js
│   ├── tsconfig.app.json
│   ├── tsconfig.json
│   ├── tsconfig.node.json
│   └── vite.config.ts
├── README.md

65 directories, 187 files

```

## Dependencies

- Node.js v20.0.0
- npm v7.24.0
- OpenJDK 21
- Maven 3.8.4
- PostgreSQL 12.0+

## Installation

### Step 1: Clone the repository

```bash
git clone https://github.com/aarch64-softdes/Group21-Ex-001
cd Group21-Ex-001
```

### Step 2: Database Setup

#### Option 1: Using Docker (Recommended)

This option requires Docker and Docker Compose to be installed on your system.

```bash
cd backend
docker-compose up -d
```

This will start a PostgreSQL database available at: `jdbc:postgresql://localhost:5432/student-db` with username `root` and password `root`.

#### Option 2: Using an existing PostgreSQL instance

If you already have PostgreSQL installed:

1. Create a new database named `student-db`
2. Run the provided schema creation script:
   ```bash
   psql -U your_username -d student-db -f database_creation.sql
   ```

### Step 3: Configure Backend Environment

1. Navigate to the backend directory:

   ```bash
   cd backend/src/main/resources
   ```

2. Create a new file named `application-dev.yml` using the template from `application.sample.yml`:

   ```bash
   cp application.sample.yml application-dev.yml
   ```

3. Edit `application-dev.yml` to match your database configuration:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/student-db
       username: root
       password: root
   ```

### Step 4: Start the Backend

```bash
cd backend
mvn spring-boot:run
```

The API server should start on port 8080 (http://localhost:8080).

### Step 5: Start the Frontend

Open a new terminal window:

```bash
cd frontend
npm install
npm run dev
```

The React development server should start on port 5173 (http://localhost:5173).

### Step 6: Access the Application

Open your browser and navigate to:

- Frontend: http://localhost:5173
- Backend API: http://localhost:8080/api

## Logging Configuration

### Elasticsearch Integration

Elasticsearch integration for advanced log management. To use this feature:

1. Install Elasticsearch using the provided script:
   ```
   curl -fsSL https://elastic.co/start-local | sh
   ```
2. Configure the connection in `application-dev.yml` with your API key
3. Set `logging.controller.logger-type: ELASTICSEARCH`

The system will create date-based indices (e.g., `logs-2025-03-19`) for easy log rotation and management.

### Setting Up Kibana Data View

After Elasticsearch is running and your application has generated some logs:

1. Open Kibana at http://localhost:5601
2. Navigate to Stack Management > Data Views
3. Click "Create data view"
4. Set the name to "Application Logs"
5. Set the index pattern to `logs-*` (matching the prefix in your config)
6. Select the timestamp field (@timestamp or timestamp)
7. Click "Save data view to Kibana"

Now you can use Discover to search and filter your logs, or create visualizations and dashboards.

## Feature Demonstrations

### Update address and identity for Student

![update-student-1](./documents/assets/SD-update-student-1.png)
![update-student-2](./documents/assets/SD-update-student-2.png)

### Allow user to manage Faculty, Status, Program

#### Faculty

- Add a new faculty:
  ![faculty-add](./documents/assets/SD-faculty-add.png)

- Edit an existing faculty:
  ![faculty-edit](./documents/assets/SD-faculty-edit.png)

#### Program

- Add a new program:
  ![program-add](./documents/assets/SD-program-add.png)

- Edit an existing program:
  ![program-edit](./documents/assets/SD-program-update.png)

#### Status

- Add a new status:
  ![status-add](./documents/assets/SD-status-add.png)

- Edit an existing status:
  ![status-edit](./documents/assets/SD-status-update.png)

### Search by name and faculty

- Search by name:
  ![search-by-name](./documents/assets/SD-search-name.png)

- Search by name and faculty:
  ![search-faculty](./documents/assets/SD-search-name-faculty.png)

### Import and Export with CSV and JSON file format

#### Exporting data

**Step 1:** Select "Export" from Student Management page, choose the file format.
![s1-export](./documents/assets/SD-export-1.png)

**Step 2:** See the file exported.
![s2-export-csv](./documents/assets/SD-export-csv.png)
![s2-export-json](./documents/assets/SD-export-json.png)

#### Importing data (using CSV file)

**Step 1:** Select "Import" from Student Management page.
![s1-import-csv](./documents/assets/SD-import-csv-1.png)

**Step 2:** Choose the CSV file.
![s2-import-csv](./documents/assets/SD-import-csv-2.png)

**Step 3:** Click "Import" to upload the file and see the data is imported.
![s3-import-csv](./documents/assets/SD-import-csv-3.png)
![s4-import-csv](./documents/assets/SD-import-csv-4.png)

#### Importing data (using JSON file)

**Step 1:** Select "Import" from Student Management page.
![s1-import-json](./documents/assets/SD-import-json-1.png)

**Step 2:** Choose the JSON file.
![s2-import-json](./documents/assets/SD-import-json-2.png)

**Step 3:** Click "Import" to upload the file and see the data is imported.
![s3-import-json](./documents/assets/SD-import-json-3.png)

### Logging mechanism

#### See logs in ElasticSearch

![logging-elasticsearch](./documents/assets/SD-logging-elastic-search.png)

#### See logs in JSON

![logging-json](./documents/assets/SD-logging-json.png)
