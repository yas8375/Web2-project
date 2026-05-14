# IMDb Movie Rental Web Application

Full-stack movie rental web application inspired by CS122B, implemented with Angular + Spring Boot.

## Team Members
- Yasmeen Otyfah - 443204580
- Samiha Nasser - 443204635
- Raghad Alyousfy - 444203521
- Aleen Alqasem - 444201194
- Roua Wadah - 443204606

## Repository Structure
- `frontend/MovieEnjoy` - Angular frontend
- `backend/movies-backend` - Spring Boot backend
- `database/schema.sql` - DB schema
- `database/movie-data.sql` - DB seed data
- `API_SPECIFICATION.md` - API/interface contract

## Local Run
### Frontend
From `frontend/MovieEnjoy`:

```powershell
cd d:\Desktop\Web2-project\frontend\MovieEnjoy
npm install
npm start
```

Frontend runs on:

```text
http://localhost:4200
```

### Backend
From `backend/movies-backend`:

```powershell
cd d:\Desktop\Web2-project\backend\movies-backend
$env:DB_URL="jdbc:postgresql://localhost:5432/your_database_name"
$env:DB_USER="your_db_user"
$env:DB_PASS="your_db_password"
.\mvnw spring-boot:run
```

Backend runs on:

```text
http://localhost:8081
```

### Notes
- The backend reads database credentials from environment variables.
- Do not commit real database credentials or secrets to GitHub.
- Start PostgreSQL locally before running Spring Boot.
- If dependencies are already installed for the frontend, `npm install` can be skipped.

## Phase 3 Deliverables Summary
This repository includes all Phase 3 testing deliverables:
- Unit tests
- Integration tests
- End-to-end (E2E) tests
- Stress/performance tests
- Robustness tests (simulated failures)
- CI workflow for automated test execution

## 1) Testing Types Implemented
### A) Unit Tests
Backend (service + controller level):
- `backend/movies-backend/src/test/java/com/example/movies_backend/service/*Test.java`
- `backend/movies-backend/src/test/java/com/example/movies_backend/controller/*Test.java`

Frontend (component-level):
- `frontend/MovieEnjoy/src/app/**/*.spec.ts`

### B) Integration Tests
Backend integration (controller + service + persistence behavior via Spring context):
- `backend/movies-backend/src/test/java/com/example/movies_backend/integration/*IntegrationTest.java`

Integration profile config:
- `backend/movies-backend/src/test/resources/application-integration.properties`

### C) End-to-End (E2E) Tests
Playwright E2E tests:
- `frontend/MovieEnjoy/tests/e2e/navigation-smoke.spec.ts`
- `frontend/MovieEnjoy/tests/e2e/main-to-movies.spec.ts`
- `frontend/MovieEnjoy/tests/e2e/single-movie-route.spec.ts`
- `frontend/MovieEnjoy/tests/e2e/browse-genres-flow.spec.ts`
- `frontend/MovieEnjoy/tests/e2e/full-journey-fail.spec.ts` (future-flow expected fail)

### D) Stress / Performance Tests
k6 HTTP stress test:
- `frontend/MovieEnjoy/tests/performance/movies-stress.js`

### E) Robustness Tests
Simulated backend failure handling:
- `backend/movies-backend/src/test/java/com/example/movies_backend/controller/MovieControllerRobustnessTest.java`

This test suite verifies safe error handling for cases like:
- database/resource failure -> 503 + safe JSON response
- unexpected runtime failure -> 500 + safe JSON response

## 2) Main Scenarios Tested
### Unit
- Service behavior, paging defaults, validation placeholders, and controller contract behavior.

### Integration
- `GET /api/movies` data retrieval with pagination/defaults.
- Future-feature integration scenarios for login/cart/checkout/movie-details/star-details (kept as planned behavior tests).

### E2E
- Top navigation flow across core pages.
- Main page -> Movies page journey.
- Direct single-movie route rendering and back navigation.
- Browse-by-genres page opening.
- Full checkout journey as future expected behavior test.

### Stress / Robustness
- Concurrent/ramping HTTP load against `/api/movies` using k6.
- Failure simulation in controller tests using mocked service exceptions.

## 3) How Stress/Robustness Testing Was Performed
### Stress (k6)
- Endpoint under load: `/api/movies?page=1&size=50`
- Scenario: ramping virtual users from 5 to 50 and back down.
- Thresholds:
  - failed request rate < 1%
  - p95 latency < 1000 ms

### Robustness (Mocked Failures)
- Backend service is mocked to throw controlled exceptions.
- Assertions verify HTTP status and safe JSON contract for error responses.

## 4) Tools and Frameworks Used
- Backend test framework: JUnit 5 + Spring Boot Test + MockMvc + Mockito
- Backend build/test runner: Maven (`mvnw`)
- Frontend unit/component tests: Angular test runner (`ng test`)
- E2E tests: Playwright
- Stress/performance tests: k6
- CI/CD: GitHub Actions (`.github/workflows/ci.yml`)

## 5) Test Commands
### Backend
From `backend/movies-backend`:

Run all backend tests (default tagging behavior):
```powershell
.\mvnw test
```

Run integration tests only:
```powershell
.\mvnw "-Dtest=*IntegrationTest" test
```

### Frontend Unit/Component
From `frontend/MovieEnjoy`:
```powershell
npm ci
npm test -- --watch=false
```

### E2E
From `frontend/MovieEnjoy`:

Stable E2E suite (for current implemented flows):
```powershell
npm run e2e:stable
```

Full E2E suite:
```powershell
npm run e2e
```

### Stress (k6)
From `frontend/MovieEnjoy`:
```powershell
npm run perf:k6:movies:smoke
```

or:
```powershell
npm run perf:k6:movies:stress
```

## 6) CI Configuration
Automated checks are defined in:
- `.github/workflows/ci.yml`

The workflow runs:
- backend build/test job (includes robustness tests, e.g., `MovieControllerRobustnessTest`)
- frontend lint/build/unit-test job
- E2E job (mock backend + full Playwright suite)
- stress job (k6 smoke test) against backend mock endpoint `/api/movies`

Stress/performance tests are now integrated in CI through the `Stress Test (k6 Smoke)` job, and can also be run locally using the npm scripts listed above.
Some E2E scenarios represent future expected behavior and may fail intentionally until the related features are fully implemented.





