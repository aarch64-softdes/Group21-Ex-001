# Inversion of Control and Dependency Injection

This Student Management System primarily uses **Spring Beans** for dependency injection across backend components. Services, repositories, and configuration classes are defined as beans and wired via constructor injection.
Spring Boot handles object creation using its dependency injection container. Components are detected via annotations and wired together at runtime.

Common stereotypes:
- `@Service` – marks service classes containing business logic.
- `@Repository` – persistence adapters that implement repository interfaces.
- `@Component` or `@Configuration` – configuration classes defining beans with `@Bean` methods.

Dependencies are injected via constructor parameters:
```java
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final TranslatorService translatorService;
}
```
The `@RequiredArgsConstructor` Lombok annotation automatically creates a constructor with the required arguments, ensuring all dependencies are supplied when the bean is created.

Custom modules can provide additional beans. When a bean of the same type exists, Spring resolves the dependency by name or qualifier. Profiles (`@Profile`) allow defining beans for different environments such as development or production.

## Qualifiers and Primary Beans
When multiple beans implement the same interface, annotate one with `@Primary` or use `@Qualifier` on the injection point to specify which bean to inject. This is common for mocking services in tests or overriding infrastructure components.

## Configuration Properties
Spring Boot maps settings from `application.yml` to POJO classes using `@ConfigurationProperties`:
```java
@ConfigurationProperties(prefix = "sms.mail")
public record MailProperties(String host, int port) {}
```
Register the properties class with `@EnableConfigurationProperties(MailProperties.class)` and inject it like any other bean.

## Collections and Optional Dependencies
Constructor injection supports `List<MyService>` for auto-wiring multiple implementations. Optional dependencies can be expressed with `@Autowired(required = false)` or `Optional<MyBean>` parameters.

## Bean Scopes and Lifecycle
Spring beans are singletons by default. Alternative scopes such as `prototype`,
`request`, or `session` can be declared with `@Scope`. Lifecycle callbacks can be
defined using `@PostConstruct` and `@PreDestroy`.

```java
@Service
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class TaskExecutor {
    @PostConstruct
    void init() { /* setup */ }
    @PreDestroy
    void cleanup() { /* tear down */ }
}
```

## Module Auto Configuration
Additional modules can contribute beans by providing a configuration class
and registering it in `spring.factories`:

```properties
org.springframework.boot.autoconfigure.EnableAutoConfiguration=\
  com.example.MyModuleAutoConfiguration
```

Spring automatically loads these configurations if the JAR is on the classpath.
Beans defined in an extra module can override application beans when annotated with
`@Primary` or when given the same bean name.

## Conditional Beans and Profiles
Use `@ConditionalOnProperty` or `@Profile` to activate beans only under certain
conditions. This pattern is helpful for environment-specific infrastructure such
as mocked services during tests.

```java
@Profile("dev")
@Configuration
public class DevMailConfig {
    @Bean
    public MailSender mailSender() { return new MockMailSender(); }
}
```

## Manual Lookup and Application Context
Although constructor injection is preferred, beans can be looked up programmatically
through `ApplicationContext` when necessary. This can be helpful when
instantiating classes reflectively or wiring beans dynamically.

```java
@Component
public class BeanHelper {
    private final ApplicationContext ctx;

    public BeanHelper(ApplicationContext ctx) {
        this.ctx = ctx;
    }

    public <T> T get(Class<T> type) {
        return ctx.getBean(type);
    }
}
```


The React frontend does not use a DI container. Components receive services via props or React context.
