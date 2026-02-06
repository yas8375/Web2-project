# API Specification Document

This document outlines the REST API endpoints for the Movie Rental Application.
It serves as the contract between Frontend and Backend.

## Base URL
`http://localhost:8081/api`

---

## 1. Movies (الأفلام)

### Get Top 20 Movies (Landing Page)
Returns the top 20 rated movies for the home page.
- **URL:** `/movies`
- **Method:** `GET`
- **Query Parameters:** None
- **Response (200 OK):**
  ```json
  [
    {
      "id": "tt001",
      "title": "The Godfather",
      "year": 1972,
      "director": "Francis Ford Coppola",
      "rating": 9.2,
      "genre": "Crime"
    },
    ...
  ]
  ```

### Search Movies
Search for movies by title, year, director, or star name.
- **URL:** `/movies/search`
- **Method:** `GET`
- **Query Parameters:**
    - `title` (optional): String
    - `year` (optional): Integer
    - `director` (optional): String
    - `star` (optional): String
- **Response (200 OK):** List of movies matching the criteria.

### Get Single Movie Details
Returns full details about a specific movie, including its cast and rating.
- **URL:** `/movies/{movieId}`
- **Method:** `GET`
- **Example:** `/movies/tt001`
- **Response (200 OK):**
  ```json
  {
    "id": "tt001",
    "title": "The Godfather",
    "year": 1972,
    "director": "Francis Ford Coppola",
    "rating": 9.2,
    "genres": ["Crime", "Drama"],
    "stars": [
      { "id": "nm001", "name": "Marlon Brando" },
      { "id": "nm002", "name": "Al Pacino" }
    ]
  }
  ```

---

## 2. Stars (الممثلين)

### Get Single Star Details
Returns information about a specific star and the movies they acted in.
- **URL:** `/stars/{starId}`
- **Method:** `GET`
- **Response (200 OK):**
  ```json
  {
    "id": "nm001",
    "name": "Marlon Brando",
    "birthYear": 1924,
    "movies": [
      { "id": "tt001", "title": "The Godfather" }
    ]
  }
  ```

---

## 3. Authentication (تسجيل الدخول)

### Login
Authenticates a user.
- **URL:** `/login`
- **Method:** `POST`
- **Request Body:** `{ "email": "user@test.com", "password": "password" }`
- **Response (200 OK):** `{ "token": "xyz...", "message": "Login successful" }`
- **Response (401 Unauthorized):** `{ "message": "Invalid credentials" }`