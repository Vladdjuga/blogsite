# Blogsite

A minimal Spring Boot 3 (Java 21) blogging backend. Focused on clean architecture, explicit API responses, and evolving authentication/authorization.

## Current Stack
- Spring Boot 3.5 (Web, Data JPA, Security, AOP, Actuator)
- PostgreSQL + Flyway for versioned DB migrations
- JWT authentication (token stored in HttpOnly cookie `accessToken`)
- Lombok (will be progressively replaced by Java records for DTOs)
- Aspect for wrapping service/controller results in a unified `Result` envelope
- Docker & Docker Compose (multi-stage build for the app + Postgres service)

## Main Features (Implemented)
- User registration and login (issue JWT, set cookie, max-age from `jwt.expirationMs`)
- Basic user listing (protected endpoint)
- Centralized error handling (global exception handler + SQL constraint logging)
- Flyway migrations with baseline + checksum validation
- JWT Filter that authenticates requests based on the cookie
- HTTP test scripts (`src/main/resources/http/*.http`) for quick manual testing (IntelliJ HTTP Client)

## Endpoints (Current)
| Method | Path | Description | Auth |
|--------|------|-------------|------|
| POST | `/api/auth/register` | Register new user | Public |
| POST | `/api/auth/login` | Authenticate & set JWT cookie | Public |
| GET  | `/api/users` | List all users | Requires JWT |

## Project Structure (Highlights)
```
src/main/java/com/vladdjuga/blogsite/
  controller/        # REST controllers (AuthController, UserController, ...)
  service/           # Business logic (AuthService, UserService, ...)
  security/          # JWT util & filter, security config
  aop/               # Result wrapper aspect
  result/            # Result & Error abstractions
  dto/               # DTOs (to be migrated to records)
  repository/        # JPA repositories
  model/entity/      # JPA entities
resources/
  db/migration/      # Flyway migration scripts
  http/              # Manual test requests
Dockerfile           # Multi-stage build
compose.yml          # App + PostgreSQL services
```

## Running Locally (Non-Docker)
Prerequisites: Java 21, Maven, PostgreSQL running on `localhost:5433` with DB `blogdb` and user `postgres/pass`.
```bash
mvn spring-boot:run
```
Visit: `http://localhost:8080`

## Running with Docker Compose
```bash
docker compose build
docker compose up -d
```
Services:
- App: http://localhost:8080
- Postgres: exposed on host port 5433

Stop & remove:
```bash
docker compose down
# or to remove volume data too
docker compose down -v
```

## Authentication Flow (Current)
1. User calls `/api/auth/login` with username/password.
2. Service validates credentials and generates JWT with expiration from `jwt.expirationMs`.
3. Token is returned as an HttpOnly `accessToken` cookie.
4. Subsequent requests include cookie; `JwtTokenFilter` extracts and validates it.
5. Spring Security context is populated -> protected endpoints accessible.

## Result Wrapping Aspect
- Methods return raw domain objects or lists.
- Aspect intercepts and wraps them into `Result<T>` for consistent API envelope.
- Future improvement: evaluate explicit vs implicit wrapping for clarity.

## Flyway Migrations
- Versioned scripts in `resources/db/migration` (V1, V2, V3, V4...)
- If a migration file changes after being applied, Flyway fails validation (checksum mismatch). Use `flyway:repair` only when intentional.
- Baseline enabled for clean onboarding.

## Manual Testing (HTTP Client)
Use IntelliJ's HTTP client file: `src/main/resources/http/users.http`.
1. Run `register_success`.
2. Run `login_success` (captures JWT cookie into global variable).
3. Run `get_users_authorized`.
4. Test failure cases (`login_fail_bad_password`, duplicate registration, etc.).

## Environment Variables (Docker)
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET` (override default in production)
- `JWT_EXPIRATIONMS` (token lifetime in ms)

## Planned / Future Enhancements
- Convert all DTOs to Java records (immutability + conciseness)
- Add role-based authorization (e.g., ADMIN vs USER)
- Refresh token & logout endpoint (rotate tokens)
- Replace cookie Strict policy with configurable SameSite Lax for cross-site needs
- Add integration tests (Testcontainers for PostgreSQL)
- CI/CD pipeline (GitHub Actions: build, test, security scan)
- Pagination & filtering for blog posts and users
- Swagger/OpenAPI documentation
- Caching layer (e.g., Redis) for frequently accessed content
- Better error model with standardized codes
- Convert `Result` wrapping to either explicit responses or use ControllerAdvice for consistency
- Central audit logging & structured logs (JSON) for production

## Development Guidelines
- Avoid editing applied Flyway migration files; create new versions instead.
- Keep service layer free of HTTP details; controllers handle transport concerns.
- Prefer constructor injection (already used via Lombok `@RequiredArgsConstructor`).
- Favor returning DTOs, not entities, from controllers.
- Keep JWT secret out of source control in real deployments (use env / secrets manager).

## Troubleshooting
| Issue | Cause | Fix |
|-------|-------|-----|
| Flyway checksum mismatch | Modified existing migration after apply | Revert change or `flyway:repair` (if intentional) |
| 401 on `/api/users` | Missing or invalid JWT cookie | Re-login to refresh token |
| Duplicate username error | Unique constraint violation | Handle gracefully in service / show validation message |
| ClassNotFound on run | Incomplete build / IDE out-of-sync | `mvn clean package` then rerun |

## License
Currently private/internal. Consider adding a LICENSE file when open-sourcing.

---
Feel free to extend this README as features evolve.

