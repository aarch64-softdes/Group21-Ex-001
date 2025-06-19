# Registering New Routes

Routes exist in both the React frontend and the Spring Boot backend. A new feature usually requires changes in both layers so URLs resolve correctly from the browser to the API.

## Backend Controllers

Controllers live under `com.tkpm.sms.presentation.controller`. Spring scans this package automatically.

1. **Create or Update a Controller**
   ```java
   @RestController
   @RequestMapping("/api/students")
   public class StudentController {
       @GetMapping("/{id}")
       public StudentDto get(@PathVariable String id) {
           return service.findById(id);
       }
   }
   ```
2. **Base Path** – All API endpoints start with `/api` followed by a resource name. Additional segments can be nested as required.
3. **Cross-Origin** – Use `@CrossOrigin` on the controller or method if your frontend is hosted on a different domain during development.

Spring automatically registers these controllers when the application starts. Each method delegates to the **application services**, which implement business rules and interact with repositories.

## Frontend Routing

React uses `react-router-dom` to define navigation paths under `frontend/src/App.tsx`. To add a new page, import the component and create a `<Route>`:

```tsx
<Routes>
  <Route path='/student/:id' element={<StudentPage />} />
</Routes>
```

Links inside the application should use `<Link>` or `useNavigate` to ensure history works correctly.

## Path Variables and Query Parameters
Use `@PathVariable` to bind segments of the URL and `@RequestParam` for query parameters. They can be combined to build flexible REST endpoints:
```java
@GetMapping("/{id}/courses")
public List<CourseDto> getCourses(@PathVariable String id, @RequestParam int year) {
    return enrollmentService.findCourses(id, year);
}
```

## Customizing Route Handling
Controllers can register interceptors through `WebMvcConfigurer` to apply logic such as logging or locale resolution. For more advanced routing, define `RouterFunction` beans using the functional style offered by Spring WebFlux, though the project primarily uses the annotation model.

### Route Constants
Avoid hard coded URLs scattered throughout the code. Common base paths can be stored in a utility class:
```java
public final class ApiPaths {
    public static final String STUDENTS = "/api/students";
    public static final String COURSES = "/api/courses";
}
```
Reference these constants from both controllers and tests to keep them in sync.

