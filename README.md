# IMDb Movie Rental Web Application

Full-stack movie rental web application using the IMDb dataset.

## Project Features
This web application offers a complete experience for browsing and renting movies based on the IMDb dataset. The key functionalities include:

### 1. Browsing and Searching
- Advanced Search: Users can search for movies using specific conditions (e.g., title, year, director).
- Browsing Categories: Browse the movie collection either by Genre or by Title.
- Movie List: View search results with options for Sorting and Pagination (Previous/Next).

### 2. Movie and Cast Details
- Single Movie Page: Displays detailed information about a selected movie, including its cast and rating.
- Single Star Page: Provides a profile for specific actors/actresses with a list of their movies.
- Hyperlinked Navigation: Allows easy navigation between movies and stars (clicking a star's name takes you to their profile).

### 3. E-Commerce Functionality
- Shopping Cart: Users can add movies to their cart to review before renting.
- Checkout Process: A dedicated flow to collect customer information and finalize the rental.
- Order Confirmation: Displays a success or failure message upon completing the transaction.

### 4. User Management & Security
- Secure Login: Users must log in with an email and password to access the system.
- Access Control: Ensures restricted pages are only accessible to authorized users.

### 5. User Interface
- Responsive Design: The interface is built using Bootstrap to ensure it works smoothly on various screen sizes.

## Team Members
- Yasmeen Otyfah - 443204580
- Samiha Nasser - 443204635
- Raghad Alyousfy - 444203521
- Aleen Alqasem - 444201194
- Roua Wadah - 443204606

## Repository Structure
- `frontend/MovieEnjoy`: Angular frontend
- `backend/movies-backend`: Spring Boot backend (Java 17)
- `database/schema.sql`: database schema script
- `database/movie-data.sql`: IMDb dataset loading script
- `API_SPECIFICATION.md`: API and interface contract for planned features

## Phase 2 Scope (What is delivered)
- Full-stack architecture setup (frontend + backend + PostgreSQL)
- IMDb dataset loaded into PostgreSQL
- Schema and data-loading scripts included
- Frontend-backend-database communication working
- API/interface specification documented for planned features

## Prerequisites
- Java 17 (Temurin recommended)
- Node.js + npm
- PostgreSQL 18 (or compatible)
- Windows PowerShell

## 1) Database Setup

### 1.1 Create database (if not created yet)
Run from PowerShell:

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -c "CREATE DATABASE moviedb;"
```

### 1.2 Load schema + data

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d moviedb -f D:\Desktop\Web2-project\database\schema.sql
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d moviedb -f D:\Desktop\Web2-project\database\movie-data.sql
```

### 1.3 Verify data is loaded

```powershell
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d moviedb -c "SELECT COUNT(*) FROM movies;"
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d moviedb -c "SELECT COUNT(*) FROM stars;"
& "C:\Program Files\PostgreSQL\18\bin\psql.exe" -U postgres -d moviedb -c "SELECT COUNT(*) FROM genres;"
```

## 2) Environment Variables (Required)
Sensitive config is read from environment variables, not hardcoded.

Backend reads:
- `DB_URL`
- `DB_USER`
- `DB_PASS`

Current backend config file:
- `backend/movies-backend/src/main/resources/application.properties`

Set variables (PowerShell, persistent):

```powershell
setx DB_URL "jdbc:postgresql://localhost:5432/moviedb"
setx DB_USER "postgres"
setx DB_PASS "<YOUR_DB_PASSWORD>"
```

Important:
- Close and reopen terminal after `setx`.
- Never commit passwords or secrets to GitHub.

Quick check:

```powershell
echo $env:DB_URL
echo $env:DB_USER
```

## 3) Run Backend

```powershell
cd D:\Desktop\Web2-project\backend\movies-backend
.\mvnw spring-boot:run
```

Expected:
- Backend starts on `http://localhost:8081`
- Log should show JDBC URL `jdbc:postgresql://localhost:5432/moviedb`

Basic endpoint test:
- `http://localhost:8081/api/movies`

## 4) Run Frontend
Open a new terminal:

```powershell
cd D:\Desktop\Web2-project\frontend\MovieEnjoy
npm install
npm start
```

Expected:
- Frontend starts on `http://localhost:4200`
- Frontend consumes backend movie data

## 5) Frontend <-> Backend Contract (Current)
Implemented and tested now:
- `GET /api/movies` from backend
- Angular frontend fetches and displays movies list/cards

## 6) API / Interface Specification
Planned feature contracts are documented in:
- `API_SPECIFICATION.md`

Includes:
- REST endpoint paths and HTTP methods
- Request parameters/payloads
- Response formats and status codes
- Authentication expectations
- Frontend-backend and backend-database mapping notes

## 7) Notes for Review / Submission
- Use Pull Requests for all changes.
- Ensure no credentials, API keys, or secrets are committed.
- Keep environment variables local only.
- Enable and pass repository checks (format/lint/tests) before merge.

## 8) Troubleshooting

### Port `8081` already in use
Stop old backend process or close old terminal running Spring Boot.

### `ERR_CONNECTION_REFUSED` on frontend
Make sure Angular app is running (`npm start`) and open `http://localhost:4200`.

### Backend starts but wrong DB (e.g., `postgres` instead of `moviedb`)
- Recheck `DB_URL`
- Reopen terminal after `setx`
- Restart backend

### Java version issue (`release version 17 not supported`)
Use Java 17 and verify:

```powershell
java -version
.\mvnw -v
```
