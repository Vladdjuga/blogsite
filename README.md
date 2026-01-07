# Blogsite

[![CI](https://github.com/vladdjuga/blogsite/actions/workflows/ci.yml/badge.svg)](https://github.com/vladdjuga/blogsite/actions/workflows/ci.yml)

A Spring Boot 3 (Java 21) blogging backend with clean architecture, JWT authentication, role-based authorization, and OpenAPI documentation.

## Tech Stack

- **Spring Boot 3.5** (Web, Data JPA, Security, AOP, Validation, Actuator)
- **PostgreSQL** + Flyway migrations
- **JWT** authentication (HttpOnly cookie)
- **OpenAPI/Swagger** documentation
- **Docker** & Docker Compose
- **GitHub Actions** CI

## Features

- ✅ User registration & login (JWT in HttpOnly cookie)
- ✅ Role-based authorization (USER, ADMIN)
- ✅ Full CRUD for Blog Posts
- ✅ Full CRUD for Users
- ✅ Owner-based access control (users can only edit/delete their own posts)
- ✅ Request validation with detailed error messages
- ✅ Centralized exception handling
- ✅ AOP-based Result wrapper pattern
- ✅ OpenAPI/Swagger UI

## API Endpoints

### Auth (Public)
| Method | Path | Description |
|--------|------|-------------|
| POST | `/api/auth/register` | Register new user |
| POST | `/api/auth/login` | Login & get JWT cookie |
| POST | `/api/auth/logout` | Logout & clear JWT cookie |

### Users
| Method | Path | Description | Access |
|--------|------|-------------|--------|
| GET | `/api/users/me` | Get current user | Authenticated |
| PUT | `/api/users/me` | Update current user | Authenticated |
| DELETE | `/api/users/me` | Delete current user | Authenticated |

### Blog Posts
| Method | Path | Description | Access |
|--------|------|-------------|--------|
| GET | `/api/posts` | Get all posts | Public |
| GET | `/api/posts/{id}` | Get post by ID | Public |
| POST | `/api/posts` | Create new post | Authenticated |
| PUT | `/api/posts/{id}` | Update post | Owner or Admin |
| DELETE | `/api/posts/{id}` | Delete post | Owner or Admin |

### Admin
| Method | Path | Description | Access |
|--------|------|-------------|--------|
| GET | `/api/admin/users` | Get all users | Admin only |
| GET | `/api/admin/users/{id}` | Get user by ID | Admin only |
| PUT | `/api/admin/users/{id}` | Update any user | Admin only |
| DELETE | `/api/admin/users/{id}` | Delete any user | Admin only |

## Project Structure

```
src/main/java/com/vladdjuga/blogsite/
├── controller/      # REST controllers
├── service/         # Business logic
├── repository/      # JPA repositories
├── model/           # Entities & enums
├── dto/             # Request/Response DTOs
├── mapper/          # Entity <-> DTO mappers
├── security/        # JWT, filters, security service
├── config/          # Security, AOP, OpenAPI configs
├── aop/             # Result wrapper aspect
├── result/          # Result & Error types
└── exception/       # Global exception handler

src/main/resources/
├── db/migration/    # Flyway SQL migrations
└── http/            # HTTP test files (IntelliJ)
```

## Running Locally

### With Docker Compose
```bash
docker-compose up
```

### Without Docker
Prerequisites: Java 21, Maven, PostgreSQL

```bash
# Set environment variables
export SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/blogdb
export SPRING_DATASOURCE_USERNAME=postgres
export SPRING_DATASOURCE_PASSWORD=pass
export JWT_SECRET=your-secret-key-min-32-characters
export JWT_EXPIRATION_MS=86400000

# Run
./mvnw spring-boot:run
```

## API Documentation

After starting the application:
- **Swagger UI:** http://localhost:8080/swagger-ui.html
- **OpenAPI JSON:** http://localhost:8080/v3/api-docs

## Running Tests

```bash
./mvnw test
```

## License

MIT

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
- Applies only to methods annotated with `@WrapResult`.
- Annotated methods MUST return `Result<T>`; otherwise an `IllegalStateException` is thrown (by the aspect).
- If the method returns `null`, the aspect logs a warning and returns `Result.fail(...)`.
- If the method returns a `Result` in failure state, the aspect logs the failure (but does not alter the returned value).
- If the method throws, the aspect logs the error and returns `Result.exception(...)` so callers receive a consistent envelope.

## Flyway Migrations
- Versioned scripts in `resources/db/migration`.
- If a migration file changes after being applied, Flyway fails validation (checksum mismatch). Use `flyway:repair` only when intentional.
- Baseline enabled for clean onboarding.

## Manual Testing (HTTP Client)
Use IntelliJ's HTTP client file: `src/main/resources/http/users.http`.
1. Run `register_success`.
2. Run `login_success` (captures JWT cookie into global variable).
3. Run `get_users_authorized`.
4. Test failure cases (`login_fail_bad_password`, duplicate registration, etc.).

## Unit & Integration Tests
- Unit tests for services (AuthService, UserService) with mocks for repositories and password encoder.
### Planned : 
- Add web layer tests for controllers using `@WebMvcTest`.
- Add integration tests with Testcontainers for PostgreSQL (Flyway migrations applied automatically).

## Environment Variables (Docker)
- `SPRING_DATASOURCE_URL`, `SPRING_DATASOURCE_USERNAME`, `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET` (override default in production)
- `JWT_EXPIRATIONMS` (token lifetime in ms)

## Planned / Future Enhancements
- Configurable cookie attributes (SameSite/secure) per environment
- Integration tests (Testcontainers for PostgreSQL)
- Pagination & filtering for blog posts and users
- Caching layer (e.g., Redis) for frequently accessed content
- Improved error model with standardized codes
- Structured JSON logs and audit logging for production

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
This project is licensed. See the `LICENSE` file in the repository root for full terms.

---
Keep this README up to date as the project evolves.
