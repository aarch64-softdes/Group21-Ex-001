# Coding Standards

Consistent style keeps the codebase readable and easy to maintain. This project follows widely adopted Java and TypeScript conventions. Code should remain self-explanatory and well documented so that new contributors can ramp up quickly.

## General Principles

- Keep functions small and focused on a single task
- Avoid exposing mutable state; favor immutability wherever practical
- Write Javadoc comments for public classes and methods explaining their purpose and side effects
- Prefer descriptive names for variables and methods; avoid abbreviations

## Java Guidelines

- **Formatting** – Spotless integrates the **google-java-format** engine so all Java code follows the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html). Indent with 4 spaces, keep lines under 120 characters and run `mvn spotless:apply` to automatically format your changes before committing.
- **Naming** – Classes use `PascalCase`, methods use `camelCase`, constants are `UPPER_SNAKE_CASE`.
- **Annotations** – Use Lombok (`@Getter`, `@Setter`, `@Builder`) to minimize boilerplate. Keep entity classes lightweight.
- **Packages** – Follow the feature-oriented structure in [`dirtree.txt`](../../dirtree.txt). Application, domain and infrastructure code live under their respective sub-packages.
- **Exception Handling** – Throw domain-specific exceptions from `com.tkpm.sms.domain.exception` and map them to HTTP responses in global handlers.
- **Logging** – Use `slf4j` (`@Slf4j`). Avoid `System.out.println` and log at the appropriate level.
- **Unit Tests** – Place JUnit tests under `backend/src/test/java` mirroring the main package structure. Name test classes with the `*Test` suffix and keep them fast.
- **Static Analysis** – The build runs `maven-checkstyle-plugin` and SpotBugs. Fix warnings before pushing.

## TypeScript / React Guidelines

- **Formatting** – ESLint and Prettier enforce the [Google TypeScript Style Guide](https://google.github.io/styleguide/tsguide.html). Formatting rules are defined in `.eslintrc` and `.prettierrc`. Run `npm run lint` and `npm run format` before committing to ensure files are automatically formatted.
- **Components** – Prefer functional components with hooks and keep them pure.
- **State Management** – Use React Context or local state; avoid unnecessary globals. Reusable logic can be extracted into custom hooks.
- **File Naming** – Component files are `PascalCase.tsx`, utilities use `camelCase.ts`.
- **Imports** – Use absolute imports starting with `@/` for project modules to avoid long relative paths.
- **Types** – Favor TypeScript interfaces for object shapes and keep types next to the code they describe.
- **Testing** – Jest and React Testing Library should accompany every component. Tests live next to the files they cover with the `.test.tsx` suffix.
- **Accessibility** – Follow WCAG guidelines. Use semantic HTML elements and provide `aria` labels where needed.

## JavaDoc and Comments
Write JavaDoc for all public classes and methods. Include a brief description, parameter documentation and return information. Inline comments should explain why a piece of code exists, not what it is doing – the code itself should be clear enough.

## Git Workflow

- Create feature branches from `work` using the pattern `<username>/<feature>`.
- Rebase frequently to keep history clean and avoid merge commits.

### Commit Messages
Follow the conventional commit style:
```
feat: add enrollment service
fix: correct grade calculation
chore: update dependencies
```
Messages should be concise, present tense and written in English. Reference GitHub issues with `#<number>` when applicable.
Use `fixup!` commits during development and squash them before merging. Commit only logical units of work to keep history easy to follow.

### Pull Requests
- Include a short summary of your changes and mention any relevant issues.
- Ensure `mvn test` and `npm run lint` complete successfully before opening a PR.
- Request at least one review. Squash merge once approvals are received.
## Branching Strategy
All development happens on short-lived feature branches. The branch name should reflect the issue or task, for example `john/do-notify-parents`. Rebase on the latest `work` branch before submitting a pull request to avoid merge conflicts.

## Reviewing Code
Pull requests are expected to include the context of the change and screenshots when UI behavior changes. Reviewers use GitHub's review tools to request modifications. Keep PRs small and focused: large multi-purpose changes are difficult to review.

## Documentation
Update Markdown files or JavaDoc when classes or APIs change. Inconsistent docs slow down onboarding and can cause misuse of the API. Treat documentation as part of the code and keep it in sync with the implementation.

