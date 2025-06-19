# Settings API

Application behavior can be tuned at runtime through entries in the `settings` table. Each row holds a unique `name` and a `details` column that stores the value, often as JSON.

The initial values are inserted by `database_creation.sql`. New environments may override defaults in `application.yml` or a profile specific file such as `application-prod.yml`.

## Domain Model
- `Setting` – simple domain object with `id`, `name` and `details` fields.
- `SettingEntity` – JPA entity mapped to the `settings` table.
- `SettingType` – enum defining canonical keys like `EMAIL`, `PHONE_NUMBER`, `ADJUSTMENT_DURATION` and `FAILING_GRADE`.

## Repository and Service
`SettingRepository` exposes typed retrieval methods. `SettingRepositoryImpl` delegates to `SettingJpaRepository` and uses Jackson to parse JSON arrays for the phone number setting.

`SettingServiceImpl` orchestrates operations. It loads a setting by name, applies validations and persists updates. List values are converted with `ObjectMapper`.

```java
@Service
@RequiredArgsConstructor
public class SettingServiceImpl implements SettingService {
    private final SettingRepository settingRepository;

    @Override
    @Transactional
    public EmailDomainSettingDto updateEmailSetting(EmailDomainSettingRequestDto dto) {
        Setting setting = settingRepository
            .findByName(SettingType.EMAIL.getValue())
            .orElseThrow(() -> new ResourceNotFoundException("error.not_found.name"));
        setting.setDetails(dto.getDomain());
        return new EmailDomainSettingDto(settingRepository.save(setting).getDetails());
    }
}
```

## Controller Endpoints
`SettingController` exposes REST endpoints under `/api/settings`:

- `GET /api/settings/phone-number` and `PUT /api/settings/phone-number`
- `GET /api/settings/email` and `PUT /api/settings/email`
- `GET /api/settings/adjustment-duration` and `PUT /api/settings/adjustment-duration`
- `GET /api/settings/failing-grade` and `PUT /api/settings/failing-grade`

## Frontend Usage
The React frontend communicates through `SettingsService` (`frontend/src/features/settings/api/settingService.ts`). React Query hooks in `useSettingsApi.ts` cache responses client-side and invalidate them after a successful update.

The backend also caches frequently accessed settings with Spring's caching abstraction. Cached values are cleared whenever a setting is updated so subsequent requests fetch the latest value from the database.

## Adding a New Setting
1. Add a value to `SettingType` and insert a default row in the SQL script.
2. Extend `SettingRepository` and `SettingRepositoryImpl` with typed getters.
3. Implement service and controller methods.
4. Create DTOs and, if needed, frontend calls and forms.
5. Update any caches or configuration classes that rely on the setting.

## Example Helper
```java
public int getMaxUploadSize() {
    return Integer.parseInt(
        settingRepository.findByName(SettingType.MAX_UPLOAD_SIZE.getValue())
            .orElseThrow()
            .getDetails());
}
```

