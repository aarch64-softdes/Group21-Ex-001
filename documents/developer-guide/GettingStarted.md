# Getting Started with App Development

## 1. Prerequisites
Before you begin, ensure you have the following installed:

- **Node.js**: v20.0.0 or newer
- **npm**: v7.24.0 or newer
- **OpenJDK**: 21
- **Maven**: 3.8.4 or newer
- **PostgreSQL**: 12.0+ (optional if using Docker)
- **Docker & Docker Compose**: Latest stable version (recommended)

## 2. Setting Up Your Development Environment

### Step 1: Clone the Repository

```bash
git clone https://github.com/aarch64-softdes/Group21-Ex-001.git
cd Group21-Ex-001
```

### Step 2: Database Setup

#### Option 1: Using Docker (Recommended)

```bash
cd backend
docker-compose up -d
```

This will start:
- PostgreSQL database: `jdbc:postgresql://localhost:5432/student-db` (username: `root`, password: `root`)
- Elasticsearch: available at `http://localhost:9200`
- Kibana: available at `http://localhost:5601`

#### Option 2: Using an Existing PostgreSQL Instance

1. Create a new database named `student-db`
2. Run the database creation script:
   ```bash
   psql -U your_username -d student-db -f database_creation.sql
   ```

### Step 3: Configure the Backend

1. Navigate to the resources directory:
   ```bash
   cd backend/src/main/resources
   ```

2. Create your development configuration:
   ```bash
   cp application.sample.yml application-dev.yml
   ```

3. Edit `application-dev.yml` with your database settings:
   ```yaml
   spring:
     datasource:
       url: jdbc:postgresql://localhost:5432/student-db
       username: root  # Change if using your own PostgreSQL
       password: root  # Change if using your own PostgreSQL
   ```
4. Activate the profile by setting `SPRING_PROFILES_ACTIVE=dev` when running locally:
   ```bash
   SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
   ```

5. Configure logging settings in `logback.xml` if needed

### Step 4: Start the Backend

```bash
cd backend
./mvnw spring-boot:run
```

> **Note:** If you're on Windows, use `mvnw.cmd` instead of `./mvnw`

The API server will start on port 8080 (http://localhost:8080).

### Step 5: Set Up the Frontend

1. Install dependencies:
   ```bash
   cd frontend
   npm install
   ```

2. Start the development server:
   ```bash
   npm run dev
   ```
The React development server will start on port 5173 (http://localhost:5173).

## 3. Development Workflow

### Backend Development

1. **Creating a new endpoint**:
   - Define DTOs in the `dto` package
   - Implement business logic in the appropriate service
   - Create a controller method to handle the request
   - Add validation and error handling
   - Write unit and integration tests

2. **Database changes**:
   - Create a new migration script in `src/main/resources/db/migration`
   - Use the naming convention `V{version}__{description}.sql`

3. **Running tests**:
   ```bash
   ./mvnw test
   ```

4. **Building the application**:
   ```bash
   ./mvnw clean package
   ```


### Frontend Development

1. **Creating a new feature**:
   - Create components in the appropriate feature folder
   - Implement API integration using the API clients
   - Add routing in the main App component if needed
   - Implement form validation using Zod and react-hook-form

2. **State Management**:
   - Use React Query for server state
   - Use React Context for global UI state
   - Prefer local component state when possible

3. **Running tests**:
   ```bash
   npm run test
   ```

4. **Building for production**:
   ```bash
   npm run build
   ```

## 4. Troubleshooting

### Common Issues

#### Backend Startup Failures

- **Database Connection Issues**: Verify PostgreSQL is running and credentials are correct
- **Port Conflicts**: Check if port 8080 is already in use
- **Lombok-related errors**: Update Lombok to at least version 1.18.38 in `pom.xml`

#### Frontend Development Issues

- **Module Not Found Errors**: Ensure all dependencies are installed with `npm install`
- **TypeScript Errors**: Check the path aliases in `tsconfig.json`
- **CORS Issues**: Verify the backend CORS configuration allows requests from the frontend