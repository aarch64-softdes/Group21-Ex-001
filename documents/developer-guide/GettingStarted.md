# Getting Started with App Development

1. **Clone the Repository**
   ```bash
   git clone https://github.com/aarch64-softdes/Group21-Ex-001
   cd Group21-Ex-001
   ```
2. **Install Prerequisites**
   - Node.js 20+
   - Java 21 and Maven 3.8+
   - Docker (for running PostgreSQL via docker-compose)
3. **Set Up the Database**
   - Navigate to `backend` and run `docker-compose up -d` to start PostgreSQL.
   - Or create the database manually and execute `database_creation.sql`.
4. **Configure the Backend**
   - Copy `backend/src/main/resources/application.sample.yml` to `application-dev.yml`.
   - Edit credentials and any optional settings (e.g., Elasticsearch).
   - Start the API server:
     ```bash
     cd backend
     mvn spring-boot:run
     ```
5. **Run the Frontend**
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
6. Browse to `http://localhost:5173` for the UI and `http://localhost:8080/api` for the API. Swagger UI is available at `http://localhost:8080/swagger-ui/index.html` once the backend is running.
7. **Configure Environment Variables**
   The backend reads database and mail server credentials from `application-dev.yml`. Use a separate file such as `application-prod.yml` for deployment and keep secrets out of version control.
   Activate the profile by setting `SPRING_PROFILES_ACTIVE=dev` when running locally:
   ```bash
   SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
   ```
   The server port defaults to `8080` but can be changed by editing `server.port` in the YAML file.
8. **Run Tests**
   To verify the environment, execute backend tests:
   ```bash
   mvn test
   ```
   Frontend unit tests can be run with:
   ```bash
   npm run test
   ```
9. **Build for Production**
   Build the backend JAR and optimized frontend assets:
   ```bash
   mvn package -DskipTests
   cd frontend && npm run build
   ```
   The contents of `frontend/dist` can be served by any static server or placed behind Nginx.

