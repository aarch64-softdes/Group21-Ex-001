# Data Validation

Input is validated both on the backend API and in the React frontend to keep data consistent and provide helpful feedback.

## API Data Validation

Validation occurs in several layers:

- **Domain Validators** – Classes in `com.tkpm.sms.domain.service.validators` check business rules before entities are persisted. They are registered via `AppConfig` and injected where needed.
- **Jakarta Bean Validation** – Request DTOs use annotations such as `@NotBlank`, `@Email` and `@Size`. These run automatically whenever a controller method receives a request.
- **Custom Annotations** – Additional checks can be added through custom constraint annotations and validators.

Example validation during student creation:
```java
studentValidator.validateStudentIdUniqueness(dto.getStudentId());
studentValidator.validateEmailUniqueness(dto.getEmail());
```

Errors are captured in a `BindingResult` and translated with `TranslatorService` so controllers return structured JSON with field names and messages.

### Validation Groups
When an entity is used in multiple contexts, validation groups allow reusing annotations but enabling or disabling certain checks. For instance, a `StudentDto` may have a `@NotNull(groups = Update.class)` constraint that only applies when updating a record.
Group sequences can combine several groups so that basic constraints run first and more expensive checks execute only if the earlier groups pass.

```java
@NotNull(groups = Basic.class)
@UniqueStudentId(groups = Advanced.class)
public class StudentDto {}

@GroupSequence({Basic.class, Advanced.class})
public interface Complete {}
```
Controllers then call `validator.validate(dto, Complete.class)` to enforce both levels of validation.

### Localized Messages
Constraint violation messages are resolved from `ValidationMessages.properties` under `src/main/resources`. Provide translations by creating language-specific files such as `ValidationMessages_vi.properties`.

### Handling Errors
A global `@ControllerAdvice` formats validation errors into a consistent structure:
```java
@RestControllerAdvice
public class ValidationErrorHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<List<FieldErrorDto>> handle(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest().body(map(ex.getBindingResult()));
    }
}
```
Clients receive a JSON array with the field name and translated message so the frontend can display inline form errors.

## FE Input Validation

React forms provide immediate feedback before submitting data to the API. All forms rely on **React Hook Form** with **Zod** schemas.

```tsx
const StudentFormSchema = z.object({
  name: z.string().min(1, t('student:validation.required')),
  email: z.string().email(t('student:validation.email')),
});

const form = useForm<StudentFormValues>({
  resolver: zodResolver(StudentFormSchema),
});
```

HTML5 validation attributes such as `required` or `type="email"` are also used for simple checks. Error messages are translated via the same i18n keys as the backend. If the API responds with additional errors, they are shown next to the related fields.

Client-side validation improves user experience but does not replace API checks. The backend always revalidates all data.

