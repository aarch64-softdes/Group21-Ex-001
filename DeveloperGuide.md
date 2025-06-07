# Developer Guide

## Agenda

## 1. Introduction

### Purpose of this Document
This Developer Guide serves as the comprehensive resource for developers working on the Student Management System (SMS). It provides detailed guidance on development standards, architectural patterns, setup procedures, and best practices to ensure consistent, maintainable, and scalable code across the entire project.

**Target Audience:** Software developers, technical leads, and DevOps engineers working on the SMS project.

### Project Scope
The Student Management System is a full-stack web application designed to manage academic operations including student enrollment, course management, and academic tracking.

**Technology Coverage:**
- **Backend**: Enterprise-grade Spring Boot 3.4.3 application with Java 21
- **Frontend**: Modern React 19 single-page application with TypeScript
- **Database**: PostgreSQL 13+ with advanced features
- **Infrastructure**: Elasticsearch for logging, Docker for containerization
- **CI/CD**: Jenkins pipeline with automated testing and deployment

### Prerequisites
Before beginning development, ensure your development environment includes:

**Required Software:**
- **Java Development Kit**: JDK 21+ (OpenJDK or Oracle JDK)
- **Build Tools**: Maven 3.6+ for dependency management
- **Node.js Runtime**: Node.js 18+ (LTS recommended) with npm
- **Database**: PostgreSQL 13+ server
- **Containerization**: Docker 20.10+ and Docker Compose 2.0+
- **Version Control**: Git 2.30+ with SSH keys configured

**Recommended IDE Setup:**
- **IntelliJ IDEA** or **Eclipse** for Java development
- **VS Code** with TypeScript and React extensions for frontend
- **Database Tools**: pgAdmin or DBeaver for PostgreSQL management

**System Requirements:**
- Minimum 8GB RAM (16GB recommended)
- 20GB free disk space
- Stable internet connection for dependency downloads

## 2. Architecture Overview

### System Architecture
The SMS follows a **Clean Architecture** pattern with **Hexagonal Architecture** principles:

```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   Presentation  │◄──►│   Application   │◄──►│   Infrastructure│
│     Layer       │    │     Layer       │    │     Layer       │
│                 │    │                 │    │                 │
│ • Controllers   │    │ • Services      │    │ • Repositories  │
│ • DTOs          │    │ • Mappers       │    │ • JPA Entities  │
│ • Validation    │    │ • Events        │    │ • Configurations│
└─────────────────┘    └─────────────────┘    └─────────────────┘
                                │
                                ▼
                       ┌─────────────────┐
                       │     Domain      │
                       │     Layer       │
                       │                 │
                       │ • Models        │
                       │ • Repositories  │
                       │ • Services      │
                       │ • Value Objects │
                       └─────────────────┘
```

### Technology Stack

#### Backend
- **Framework**: Spring Boot 3.4.3 with Java 21
- **Database**: PostgreSQL with JPA/Hibernate
- **Logging**: Elasticsearch with custom logger implementation
- **Validation**: Jakarta Bean Validation
- **Mapping**: MapStruct for entity-DTO mapping
- **Documentation**: OpenAPI/Swagger
- **Testing**: JUnit 5, Mockito, Spring Boot Test

#### Frontend
- **Framework**: React 19 with TypeScript
- **Build Tool**: Vite 6.2.0
- **Styling**: Tailwind CSS 4.0.12 with Radix UI components
- **Routing**: React Router 7.4.0
- **State Management**: React Query (TanStack Query) for server state
- **Forms**: React Hook Form with Zod validation
- **Internationalization**: i18next with react-i18next

### Data Flow
```
User Interface → API Controllers → Application Services → Domain Services → Repository → Database
     ↑                                                                                      ↓
Frontend (React) ←── JSON Response ←── DTO Mapping ←── Domain Models ←── JPA Entities ←────┘
```

## 3. Coding Standards

### Naming Conventions
- **Java**: 
  - Classes: `PascalCase` (e.g., `StudentController`)
  - Methods/Variables: `camelCase` (e.g., `findStudentById`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `DEFAULT_PAGE_SIZE`)
  - Packages: `lowercase.separated.by.dots`

- **TypeScript/JavaScript**:
  - Components: `PascalCase` (e.g., `StudentForm`)
  - Variables/Functions: `camelCase` (e.g., `handleSubmit`)
  - Constants: `UPPER_SNAKE_CASE` (e.g., `API_BASE_URL`)
  - Files: `kebab-case` for regular files, `PascalCase` for components

### Code Formatting
- **Indentation**: 4 spaces for Java, 2 spaces for TypeScript/JavaScript
- **Line Length**: Maximum 120 characters
- **Backend**: Use `eclipse-formatter.xml` configuration
- **Frontend**: ESLint + Prettier configuration in `eslint.config.js`

### Pre-commit Setup
```bash
# Install Husky for pre-commit hooks
npm install -g husky
husky init
echo "npm run lint && npm run test" > .husky/pre-commit
```

## 4. Project Structure

### Backend Structure (`/backend`)
```
src/
├── main/
│   ├── java/com/tkpm/sms/
│   │   ├── domain/                    # Domain layer (business logic)
│   │   │   ├── model/                 # Domain entities
│   │   │   ├── repository/            # Repository interfaces
│   │   │   ├── service/               # Domain services
│   │   │   └── valueobject/           # Value objects
│   │   ├── application/               # Application layer
│   │   │   ├── dto/                   # Data Transfer Objects
│   │   │   ├── mapper/                # DTO-Domain mappers
│   │   │   └── service/               # Application services
│   │   ├── infrastructure/            # Infrastructure layer
│   │   │   ├── persistence/           # Data persistence
│   │   │   │   ├── entity/            # JPA entities
│   │   │   │   ├── repository/        # Repository implementations
│   │   │   │   └── mapper/            # Entity-Domain mappers
│   │   │   ├── config/                # Configuration classes
│   │   │   └── logging/               # Custom logging implementation
│   │   └── presentation/              # Presentation layer
│   │       └── controller/            # REST controllers
│   └── resources/
│       ├── application.yml            # Main configuration
│       ├── application.sample.yml     # Sample configuration
│       └── messages-i18n*.properties  # Internationalization
└── test/                              # Test classes mirror main structure
```

### Frontend Structure (`/frontend`)
```
src/
├── main.tsx                          # Application entry point
├── App.tsx                           # Root component with routing
├── features/                         # Feature-based organization
│   ├── student/                      # Student management feature
│   │   ├── api/                      # API services and hooks
│   │   ├── components/               # Feature-specific components
│   │   ├── types/                    # TypeScript type definitions
│   │   └── studentPage.tsx           # Main page component
│   ├── enrollment/                   # Enrollment management
│   ├── subject/                      # Subject management
│   └── course/                       # Course management
├── shared/                           # Shared utilities and components
│   ├── components/                   # Reusable UI components
│   ├── hooks/                        # Custom React hooks
│   ├── utils/                        # Utility functions
│   ├── types/                        # Global type definitions
│   └── i18n/                         # Internationalization setup
└── core/                             # Core application setup
    ├── api/                          # Base API configuration
    └── router/                       # Router configuration
```

## 5. Getting Started

### Environment Setup

1. **Clone the repository**:
```bash
git clone <repository-url>
cd Group21-Ex-001
```

2. **Backend Setup**:
```bash
cd backend
cp src/main/resources/application.sample.yml src/main/resources/application.yml
# Edit application.yml with your database credentials
./mvnw clean install
```

3. **Frontend Setup**:
```bash
cd frontend
npm install
# Create .env file if needed
echo "VITE_API_URL=http://localhost:8080" > .env
```

4. **Database Setup**:
```bash
# Create PostgreSQL database
createdb student-db
# Run the application - tables will be created automatically
```

### Running the Application

#### Development Mode
```bash
# Backend (Terminal 1)
cd backend
./mvnw spring-boot:run

# Frontend (Terminal 2)
cd frontend
npm run dev
```

#### Production Build
```bash
# Backend
cd backend
./mvnw clean package
java -jar target/sms-0.0.1-SNAPSHOT.jar

# Frontend
cd frontend
npm run build
npm run preview
```

### Common Commands
```bash
# Backend
./mvnw test                    # Run tests
./mvnw test -Dtest=ClassTest   # Run specific test
./mvnw clean package           # Build JAR
./mvnw spring-boot:run         # Run in development

# Frontend
npm run dev                    # Start development server
npm run build                  # Build for production
npm run lint                   # Run ESLint
npm run test                   # Run tests (if configured)
```
  ## 6. Backend Development

### 6.1 Database Schema
The system uses PostgreSQL with JPA/Hibernate for ORM. Key entities include:

**Core Entities:**
- `students` - Student information with soft delete support
- `faculties` - Academic faculties
- `programs` - Academic programs
- `subjects` - Course subjects with prerequisites
- `courses` - Course instances with schedules
- `enrollments` - Student course enrollments
- `statuses` - Student status management

**Relationship Overview:**
```sql
students ──► faculties (Many-to-One)
students ──► programs (Many-to-One) 
students ──► statuses (Many-to-One)
courses ──► subjects (Many-to-One)
courses ──► programs (Many-to-One)
enrollments ──► students (Many-to-One)
enrollments ──► courses (Many-to-One)
```

**Database Creation:**
See `database_creation.sql` for complete DDL. The application uses `ddl-auto: update` for automatic schema updates.

### 6.2 Adding a New Entity

Follow these steps to add a new entity:

1. **Create Domain Model** (`domain/model/`):
```java
@Data
@Builder
public class NewEntity {
    private String id;
    private String name;
    // ... other fields
}
```

2. **Create Repository Interface** (`domain/repository/`):
```java
public interface NewEntityRepository {
    NewEntity save(NewEntity entity);
    Optional<NewEntity> findById(String id);
    // ... other methods
}
```

3. **Create JPA Entity** (`infrastructure/persistence/entity/`):
```java
@Entity
@Table(name = "new_entities")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NewEntityEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    
    @Column(nullable = false)
    private String name;
}
```

4. **Create JPA Repository** (`infrastructure/persistence/jpa/`):
```java
public interface NewEntityJpaRepository extends JpaRepository<NewEntityEntity, String> {
    // Custom query methods
}
```

5. **Create Persistence Mapper** (`infrastructure/persistence/mapper/`):
```java
@Mapper(componentModel = "spring")
public interface NewEntityPersistenceMapper {
    NewEntity toDomain(NewEntityEntity entity);
    NewEntityEntity toEntity(NewEntity domain);
}
```

6. **Implement Repository** (`infrastructure/persistence/repository/`):
```java
@Repository
@RequiredArgsConstructor
public class NewEntityRepositoryImpl implements NewEntityRepository {
    private final NewEntityJpaRepository jpaRepository;
    private final NewEntityPersistenceMapper mapper;
    
    @Override
    public NewEntity save(NewEntity entity) {
        var entityToSave = mapper.toEntity(entity);
        var savedEntity = jpaRepository.save(entityToSave);
        return mapper.toDomain(savedEntity);
    }
}
```

### 6.3 Creating New API Endpoints

1. **Create DTOs** (`application/dto/request/` and `application/dto/response/`):
```java
// Request DTO
@Data
public class NewEntityCreateRequestDto {
    @NotBlank
    private String name;
}

// Response DTO
@Data
public class NewEntityDto {
    private String id;
    private String name;
}
```

2. **Create Application Mapper** (`application/mapper/`):
```java
@Mapper(componentModel = "spring")
public interface NewEntityMapper {
    NewEntity toDomain(NewEntityCreateRequestDto dto);
    NewEntityDto toDto(NewEntity domain);
}
```

3. **Create Service** (`application/service/implementation/`):
```java
@Service
@RequiredArgsConstructor
@Transactional
public class NewEntityServiceImpl implements NewEntityService {
    private final NewEntityRepository repository;
    private final NewEntityMapper mapper;
    
    public NewEntityDto create(NewEntityCreateRequestDto request) {
        var domain = mapper.toDomain(request);
        var saved = repository.save(domain);
        return mapper.toDto(saved);
    }
}
```

4. **Create Controller** (`presentation/controller/`):
```java
@RestController
@RequestMapping("/api/new-entities")
@RequiredArgsConstructor
public class NewEntityController {
    private final NewEntityService service;
    
    @PostMapping
    public ResponseEntity<ApplicationResponseDto<NewEntityDto>> create(
            @Valid @RequestBody NewEntityCreateRequestDto request) {
        var result = service.create(request);
        return ResponseEntity.ok(ApplicationResponseDto.success(result));
    }
}
```

### 6.4 Dependency Injection & IoC

The application uses **Spring Boot's IoC container** with constructor injection:

```java
@Service
@RequiredArgsConstructor  // Lombok generates constructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final TranslatorService translatorService;
    
    // Spring automatically injects dependencies through constructor
}
```

**Configuration Beans** (`infrastructure/config/AppConfig.java`):
```java
@Configuration
public class AppConfig {
    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
    
    @Bean
    public StudentDomainValidator studentValidator(
            StudentRepository repository,
            TranslatorService translatorService) {
        return new StudentDomainValidator(repository, translatorService);
    }
}
```

### 6.5 Data Validation

**Bean Validation** with Jakarta annotations:
```java
public class StudentCreateRequestDto {
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[\\p{L}\\s]*$", message = "Name must contain only letters and spaces")
    private String name;
    
    @Email(message = "Invalid email format")
    @NotNull
    private String email;
    
    @Pattern(regexp = "^\\+\\d{1,3}\\d{6,12}$", 
             message = "Phone must be in international format")
    private String phone;
}
```

**Custom Validation** example:
```java
@Component
public class StudentDomainValidator {
    public void validateStudentCreation(Student student) {
        if (repository.existsByStudentId(student.getStudentId())) {
            throw new ValidationException("Student ID already exists");
        }
        // Additional business rules...
    }
}
```

### 6.6 Event Handling

**Spring Events** for decoupled communication:
```java
// Event class
public class StudentEnrolledEvent {
    private final String studentId;
    private final Integer courseId;
    // constructor, getters...
}

// Publishing events
@Service
public class EnrollmentService {
    private final ApplicationEventPublisher eventPublisher;
    
    public void enrollStudent(String studentId, Integer courseId) {
        // ... enrollment logic
        eventPublisher.publishEvent(new StudentEnrolledEvent(studentId, courseId));
    }
}

// Event listener
@Component
public class EnrollmentEventListener {
    @EventListener
    public void handleStudentEnrolled(StudentEnrolledEvent event) {
        // Handle the event (send notification, update statistics, etc.)
    }
}
```

### 6.7 Configuration Management

**Application Properties** (`application.yml`):
```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/student-db
    username: ${DB_USER:root}
    password: ${DB_PASSWORD:root}

app:
  locale:
    default: en
    supported: en,vi
  constraint:
    deletable-duration: 30
```

**Configuration Properties Class**:
```java
@ConfigurationProperties(prefix = "app")
@Data
public class AppProperties {
    private Locale locale = new Locale();
    private Constraint constraint = new Constraint();
    
    @Data
    public static class Locale {
        private String defaultLocale = "en";
        private List<String> supported = List.of("en", "vi");
    }
}
```

### 6.8 Plugin Architecture

The current system doesn't have a formal plugin system, but you can extend functionality using:

**Custom Starters** for reusable modules:
```java
@Configuration
@EnableConfigurationProperties(PluginProperties.class)
public class PluginAutoConfiguration {
    @Bean
    @ConditionalOnProperty(name = "plugin.enabled", havingValue = "true")
    public PluginService pluginService() {
        return new PluginServiceImpl();
    }
}
```

### 6.9 API Documentation

* **api-docs with specific format**: 
  * *JSON*: `http://localhost:8080/api-docs`
  * *YAML*: `http://localhost:8080/api-docs.yaml`

* **OpenAPI/Swagger** setup:</br> 
  Already configured - access at: `http://localhost:8080/swagger-ui.html`

* **Documenting endpoints**:
  ```java
  @RestController
  @Tag(name = "Student Management", description = "APIs for managing students")
  public class StudentController {
      
      @Operation(summary = "Create a new student", description = "Creates a new student with the provided information")
      @ApiResponses(value = {
          @ApiResponse(responseCode = "200", description = "Student created successfully"),
          @ApiResponse(responseCode = "400", description = "Invalid input"),
          @ApiResponse(responseCode = "409", description = "Student already exists")
      })
      @PostMapping
      public ResponseEntity<ApplicationResponseDto<StudentDto>> createStudent(
              @io.swagger.v3.oas.annotations.parameters.RequestBody(
                  description = "Student information",
                  required = true,
                  content = @Content(schema = @Schema(implementation = StudentCreateRequestDto.class))
              )
              @Valid @RequestBody StudentCreateRequestDto request) {
          // implementation...
      }
  }
```

## 7. Frontend Development

### 7.1 Technology Stack & Architecture

**Core Technologies:**
- **React 19** with TypeScript for type safety
- **Vite** for fast development and building
- **Tailwind CSS** with Radix UI components for styling
- **React Router 7** for client-side routing
- **TanStack Query** for server state management
- **React Hook Form** with Zod for form validation
- **i18next** for internationalization

### 7.2 Component Architecture

**Component Hierarchy:**
```
App.tsx (Root)
├── Layout Components
│   ├── Header
│   ├── Sidebar
│   └── Footer
├── Feature Pages
│   ├── StudentPage
│   ├── EnrollmentPage
│   └── SubjectPage
└── Shared Components
    ├── UI Components (Button, Input, etc.)
    ├── Forms
    └── Tables
```

**Component Convention:**
```typescript
// PascalCase for component files and names
// Use function components with hooks

interface StudentFormProps {
  onSubmit: (data: StudentCreateRequest) => void;
  initialData?: Student;
}

export const StudentForm: React.FC<StudentFormProps> = ({ 
  onSubmit, 
  initialData 
}) => {
  // Component logic here
  return (
    <form onSubmit={handleSubmit}>
      {/* JSX here */}
    </form>
  );
};
```

### 7.3 State Management

**Server State with TanStack Query:**
```typescript
// API hooks for server state management
export const useStudents = (params: StudentFilters) => {
  return useQuery({
    queryKey: ['students', params],
    queryFn: () => studentService.getStudents(params),
    staleTime: 5 * 60 * 1000, // 5 minutes
  });
};

// Mutations for server updates
export const useCreateStudent = () => {
  const queryClient = useQueryClient();
  
  return useMutation({
    mutationFn: studentService.createStudent,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['students'] });
    },
  });
};
```

**Local State with React Hooks:**
```typescript
// Use useState for component-level state
const [isModalOpen, setIsModalOpen] = useState(false);

// Use useReducer for complex state logic
const [formState, dispatch] = useReducer(formReducer, initialState);
```

### 7.4 Routing

**React Router Setup** (`App.tsx`):
```typescript
import { createBrowserRouter, RouterProvider } from 'react-router-dom';

const router = createBrowserRouter([
  {
    path: '/',
    element: <Layout />,
    children: [
      { path: 'students', element: <StudentPage /> },
      { path: 'students/:id', element: <StudentDetailPage /> },
      { path: 'enrollments', element: <EnrollmentPage /> },
      { path: 'subjects', element: <SubjectPage /> },
    ],
  },
]);

export default function App() {
  return <RouterProvider router={router} />;
}
```

### 7.5 Form Handling & Validation

**React Hook Form with Zod:**
```typescript
import { useForm } from 'react-hook-form';
import { zodResolver } from '@hookform/resolvers/zod';
import { z } from 'zod';

const studentSchema = z.object({
  name: z.string().min(1, 'Name is required'),
  email: z.string().email('Invalid email format'),
  phone: z.string().regex(/^\+\d{1,3}\d{6,12}$/, 'Invalid phone format'),
});

type StudentFormData = z.infer<typeof studentSchema>;

export const StudentForm = () => {
  const {
    register,
    handleSubmit,
    formState: { errors, isSubmitting },
  } = useForm<StudentFormData>({
    resolver: zodResolver(studentSchema),
  });

  const onSubmit = async (data: StudentFormData) => {
    // Handle form submission
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)}>
      <input 
        {...register('name')} 
        className="input"
      />
      {errors.name && <span className="error">{errors.name.message}</span>}
      
      <button type="submit" disabled={isSubmitting}>
        {isSubmitting ? 'Saving...' : 'Save'}
      </button>
    </form>
  );
};
```

### 7.6 Styling with Tailwind CSS

**Component Styling:**
```typescript
// Use Tailwind classes for styling
export const Button = ({ variant, children, ...props }) => {
  const baseClasses = "px-4 py-2 rounded font-medium transition-colors";
  const variantClasses = {
    primary: "bg-blue-600 text-white hover:bg-blue-700",
    secondary: "bg-gray-200 text-gray-900 hover:bg-gray-300",
  };
  
  return (
    <button 
      className={`${baseClasses} ${variantClasses[variant]}`}
      {...props}
    >
      {children}
    </button>
  );
};
```

**Tailwind Configuration** (`tailwind.config.js`):
```javascript
export default {
  content: ['./src/**/*.{js,ts,jsx,tsx}'],
  theme: {
    extend: {
      colors: {
        primary: {
          50: '#eff6ff',
          500: '#3b82f6',
          900: '#1e3a8a',
        },
      },
    },
  },
  plugins: [],
};
```

### 7.7 API Integration

**API Service Layer:**
```typescript
// Base API configuration
const api = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080',
  headers: {
    'Content-Type': 'application/json',
  },
});

// Feature-specific service
export const studentService = {
  getStudents: async (params: StudentFilters): Promise<PageResponse<Student>> => {
    const response = await api.get('/api/students', { params });
    return response.data;
  },
  
  createStudent: async (data: StudentCreateRequest): Promise<Student> => {
    const response = await api.post('/api/students', data);
    return response.data;
  },
  
  updateStudent: async (id: string, data: StudentUpdateRequest): Promise<Student> => {
    const response = await api.patch(`/api/students/${id}`, data);
    return response.data;
  },
};
```

### 7.8 Internationalization

**i18next Setup:**
```typescript
// i18n configuration
import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';

i18n
  .use(initReactI18next)
  .init({
    resources: {
      en: { translation: require('./locales/en/common.json') },
      vi: { translation: require('./locales/vi/common.json') },
    },
    lng: 'en',
    fallbackLng: 'en',
    interpolation: { escapeValue: false },
  });

// Usage in components
import { useTranslation } from 'react-i18next';

export const StudentForm = () => {
  const { t } = useTranslation();
  
  return (
    <form>
      <label>{t('student.name')}</label>
      <input placeholder={t('student.namePlaceholder')} />
    </form>
  );
};
```

### 7.9 Testing

**Jest + React Testing Library Setup:**
```typescript
// Component test example
import { render, screen, fireEvent } from '@testing-library/react';
import { StudentForm } from './StudentForm';

describe('StudentForm', () => {
  it('should submit form with valid data', async () => {
    const mockSubmit = jest.fn();
    render(<StudentForm onSubmit={mockSubmit} />);
    
    fireEvent.change(screen.getByLabelText(/name/i), {
      target: { value: 'John Doe' },
    });
    
    fireEvent.click(screen.getByRole('button', { name: /save/i }));
    
    await waitFor(() => {
      expect(mockSubmit).toHaveBeenCalledWith({
        name: 'John Doe',
      });
    });
  });
});
```

### 7.10 Performance Optimization

**Code Splitting:**
```typescript
// Lazy load components
const StudentPage = lazy(() => import('./features/student/StudentPage'));
const EnrollmentPage = lazy(() => import('./features/enrollment/EnrollmentPage'));

// Use Suspense for loading states
<Suspense fallback={<LoadingSpinner />}>
  <StudentPage />
</Suspense>
```

**Memoization:**
```typescript
// Memoize expensive calculations
const expensiveValue = useMemo(() => {
  return computeExpensiveValue(data);
}, [data]);

// Memoize components to prevent unnecessary re-renders
const StudentCard = memo(({ student }) => {
  return <div>{student.name}</div>;
});
```

## 8. Testing Strategy

### 8.1 Backend Testing

**Unit Tests with JUnit 5:**
```java
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository studentRepository;
    
    @Mock
    private StudentMapper studentMapper;
    
    @InjectMocks
    private StudentServiceImpl studentService;
    
    @Test
    void shouldCreateStudentSuccessfully() {
        // Given
        var request = StudentCreateRequestDto.builder()
            .name("John Doe")
            .email("john@example.com")
            .build();
        
        var domain = Student.builder()
            .name("John Doe")
            .email("john@example.com")
            .build();
            
        when(studentMapper.toDomain(request)).thenReturn(domain);
        when(studentRepository.save(domain)).thenReturn(domain);
        
        // When
        var result = studentService.createStudent(request);
        
        // Then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("John Doe");
        verify(studentRepository).save(domain);
    }
}
```

**Integration Tests:**
```java
@SpringBootTest
@Testcontainers
@AutoConfigTestDatabase(replace = AutoConfigTestDatabase.Replace.NONE)
class StudentRepositoryIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:13")
            .withDatabaseName("testdb")
            .withUsername("test")
            .withPassword("test");
    
    @Autowired
    private StudentRepository studentRepository;
    
    @Test
    void shouldSaveAndRetrieveStudent() {
        // Test implementation
    }
}
```

**Test Coverage Target:** Minimum 80% line coverage for business logic.

### 8.2 Frontend Testing

**Component Testing:**
```typescript
import { render, screen } from '@testing-library/react';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { StudentList } from './StudentList';

const createTestQueryClient = () => new QueryClient({
  defaultOptions: { queries: { retry: false } },
});

describe('StudentList', () => {
  it('should display students when data is loaded', () => {
    const queryClient = createTestQueryClient();
    
    render(
      <QueryClientProvider client={queryClient}>
        <StudentList />
      </QueryClientProvider>
    );
    
    expect(screen.getByText(/loading/i)).toBeInTheDocument();
  });
});
```

**Testing Commands:**
```bash
# Backend
./mvnw test                           # All tests
./mvnw test -Dtest=StudentServiceTest # Specific test
./mvnw verify                         # Tests + integration tests

# Frontend
npm run test                          # Run tests (if configured)
npm run test:watch                    # Watch mode
npm run test:coverage                 # Coverage report
```

## 9. Deployment & CI/CD
- Pipeline (Jenkins/GitHub Actions): lint → test → build → deploy.
- Jenkinsfile có sẵn trong root.
- Rollback strategy: sử dụng Docker image tags.

## 10. Contribution Guide
- Fork → PR → review.
- Checklist: coding standards, test coverage, doc update.
- Issue template trong `.github/ISSUE_TEMPLATE`.

## 11. FAQ / Troubleshooting
...
