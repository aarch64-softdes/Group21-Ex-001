# Updating an Existing Entity

Follow these steps when you need to add a new field to a domain model. The example below adds a `nickname` property to the `Student` entity.

## 1. Modify the Domain Model

- Edit the class under `backend/src/main/java/com/tkpm/sms/domain/model`.
- Use Lombok annotations to generate boilerplate accessors.

```java
@Getter
@Setter
public class Student {
    private String nickname; // new property
}
```

If constructors or builders exist, include the new field there as well.

## 2. Adjust Persistence Mapping

- Update the JPA entity in `com.tkpm.sms.infrastructure.persistence.entity`.
- Add a `@Column` definition if the property requires a specific length or constraint.
- Regenerate `equals` and `hashCode` if they depend on all fields.

## 3. Create a Liquibase Migration

Create a new file under `backend/src/main/resources/db/changelog` with the next sequence number, for example `db.changelog-002.xml`:

```xml
<changeSet id="002-add-nickname" author="dev">
    <addColumn tableName="students">
        <column name="nickname" type="varchar(255)"/>
    </addColumn>
</changeSet>
```

Run `mvn liquibase:update` to apply the migration locally. This keeps all environments in sync.

## 4. Seed or Update Existing Data

If the new column cannot be null, supply an update statement in the same changeset:

```xml
<update tableName="students">
    <column name="nickname" value=""/>
</update>
```

Avoid manual database edits so that every deployment starts from the same schema history.

## 5. Update Repository and Service Layers

- Repository interfaces based on Spring Data JPA usually require no changes.
- Ensure service classes map incoming DTOs to the new entity field and persist it.

## 6. Update DTOs and MapStruct Mappers

- Add the property to request and response DTO classes in `com.tkpm.sms.application.dto`.
- Update `@Mapper` interfaces accordingly and run `mvn compile` so MapStruct regenerates the implementations.

## 7. Update Controllers and API Documentation

- Modify controller methods to accept and return the field.
- Regenerate Swagger/OpenAPI docs via Springdoc. Document the change in [`WebAPI.md`](WebAPI.md).

## 8. Frontend Adjustments

- Extend the TypeScript models in `frontend/src/core`.
- Update forms and React Hook Form schemas to capture the new property.
- Provide translated labels and validation messages in the i18n files.

## 9. Regression Tests

Add unit tests covering both persistence and validation of the property. Mock repositories where appropriate. Integration tests are not part of the project, but if they are introduced in the future, you can use Testcontainers to verify migrations against a temporary PostgreSQL instance.

