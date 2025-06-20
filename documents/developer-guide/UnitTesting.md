# Unit Testing

All automated tests reside under `backend/src/test/java`. We rely on JUnit 5 for the test framework and Mockito to mock collaborators. Unit tests should target a single class, isolating it from external systems such as the database or web layer.

Run tests with:
```bash
mvn test
```

The Maven Surefire plugin runs the tests when you execute `mvn test`. The Jacoco plugin automatically generates a coverage report in `target/site/jacoco`. Aim for high branch and instruction coverage by mocking repositories and services rather than interacting with a real database.

For frontend code, Jest and React Testing Library provide a lightweight way to test components and hooks. Keep tests focused on component behaviour and avoid heavy DOM setups.
## Best Practices
- Keep unit tests small and self contained. Each test should verify a single behaviour.
- Use descriptive method names following the given/when/then pattern.
- Initialize mocks with `@ExtendWith(MockitoExtension.class)` or `MockitoAnnotations.openMocks(this)`.
- Group related tests using nested classes annotated with `@Nested` and `@DisplayName`.
- Use helper methods or builders to create domain objects and keep tests readable.
- Verify interactions with mocks using `verify` and `verifyNoMoreInteractions`.
- Use `assertThrows` to assert expected exceptions.
- Parameterize tests with `@ParameterizedTest` for multiple input scenarios.
- Avoid randomness and external dependencies; inject clocks or stub collaborators.

## Service Example
Service classes are typically tested with Mockito by mocking repository dependencies. The `@ExtendWith(MockitoExtension.class)` annotation initializes mocks and allows the use of the `@InjectMocks` helper:

```java
@ExtendWith(MockitoExtension.class)
class StudentServiceTest {
    @Mock
    private StudentRepository repository;

    @InjectMocks
    private StudentService service;

    @Test
    void createsStudent_whenDataValid() {
        Student student = new Student("Anna");
        when(repository.save(student)).thenReturn(student);

        Student result = service.create(student);

        assertEquals("Anna", result.getName());
        verify(repository).save(student);
    }
}
```

## Test Naming
Name test classes after the component under test followed by `Test`. Individual test methods should clearly state the condition being verified, for example `createsStudent_whenDataValid`. Avoid large setup blocks by using helper methods or test fixtures.

Integration tests are not currently part of the project. If you introduce them later, consider using `@SpringBootTest` along with Testcontainers to provide an ephemeral database instance.

## Coverage Thresholds
Pull requests should strive for at least 80% line coverage on new code. Jacoco's HTML report helps identify untested branches.

Run `mvn test` with the `jacoco` profile to generate coverage metrics:
```bash
mvn test -Pjacoco
open target/site/jacoco/index.html
```
Frontend coverage is reported via `npm run test -- --coverage` and stored under `frontend/coverage`.

