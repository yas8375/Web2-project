# API Specification

Backend base URL: `http://localhost:8081/api`

This file is organized by controller, matching current backend code.

## Phase 2 Status
- Implemented and connected to DB:
  - `GET /api/movies`
  - `GET /api/movies/{movieId}`
- Contract only (no full implementation yet, returns `501 Not Implemented`):
  - login, genres, stars, cart, checkout endpoints.

---

## MovieController
File: `backend/movies-backend/src/main/java/com/example/movies_backend/controller/MovieController.java`

### GET `/api/movies`
- Logic: Return movie list. Supports search/sort/pagination query params.
- Params (all optional):
  - `title: string`
  - `year: number`
  - `director: string`
  - `star: string`
  - `genre: string`
  - `letter: string`
  - `sort: string` (default `title`)
  - `order: string` (default `asc`)
  - `page: number` (default `1`)
  - `size: number` (default `10`)
- Return:
  - `200 OK` with JSON array of movies.

Example response:
```json
[
  {
    "id": "tt0421974",
    "title": "Sky Fighters",
    "year": 2005,
    "director": "Gerard Pires"
  }
]
```

### GET `/api/movies/{movieId}`
- Logic: Return one movie by id.
- Params:
  - `movieId: string` (path)
- Return:
  - `200 OK` with movie JSON if found
  - `404 Not Found` if movie does not exist

### GET `/api/genres`
- Logic: Contract endpoint for genres list.
- Params: none
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

---

## AuthController
File: `backend/movies-backend/src/main/java/com/example/movies_backend/controller/AuthController.java`

### POST `/api/login`
- Logic: Contract endpoint for customer login.
- Params (request body):
```json
{
  "email": "user@example.com",
  "password": "secret"
}
```
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

---

## StarController
File: `backend/movies-backend/src/main/java/com/example/movies_backend/controller/StarController.java`

### GET `/api/stars/{starId}`
- Logic: Contract endpoint for single star details.
- Params:
  - `starId: string` (path)
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

---

## CartController
File: `backend/movies-backend/src/main/java/com/example/movies_backend/controller/CartController.java`

### GET `/api/cart`
- Logic: Contract endpoint to read current cart.
- Params: none
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

### POST `/api/cart/items`
- Logic: Contract endpoint to add item to cart.
- Params (request body):
```json
{
  "movieId": "tt0421974",
  "quantity": 1
}
```
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

### PUT `/api/cart/items/{movieId}`
- Logic: Contract endpoint to update cart item quantity.
- Params:
  - `movieId: string` (path)
  - request body:
```json
{
  "quantity": 2
}
```
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

### DELETE `/api/cart/items/{movieId}`
- Logic: Contract endpoint to remove item from cart.
- Params:
  - `movieId: string` (path)
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

---

## CheckoutController
File: `backend/movies-backend/src/main/java/com/example/movies_backend/controller/CheckoutController.java`

### POST `/api/checkout`
- Logic: Contract endpoint for checkout and payment submission.
- Params (request body example):
```json
{
  "firstName": "Ali",
  "lastName": "Ahmed",
  "cardNumber": "1234567890123456",
  "expiration": "2030-12"
}
```
- Return:
  - `501 Not Implemented` (Phase 2 contract only)

---

## Frontend-Backend Contract (Current)
- Angular frontend calls `GET /api/movies` and renders movie list/cards.
- Other endpoints are present as API contracts for later phases.
