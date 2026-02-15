# API Specification

Movie Rental Application REST contract shared between frontend, backend, and database.

## Base URL
`http://localhost:8081/api`

## Phase 2 Implementation Status
- Implemented now: `GET /api/movies`
- Planned for next phases: all other endpoints in this file

---

## 1) Login

### Endpoint
`POST /api/auth/login`

### Auth
Not required.

### Request
Headers:
- `Content-Type: application/json`

Body:
```json
{
  "email": "user@example.com",
  "password": "P@ssw0rd!"
}
```

### Validation
- `email` required, valid email format
- `password` required, non-empty

### Responses
- `200 OK`
```json
{
  "success": true,
  "message": "Login successful",
  "user": {
    "id": 12,
    "name": "Ali Ahmed",
    "email": "user@example.com"
  }
}
```

- `400 Bad Request`
```json
{
  "success": false,
  "message": "Validation error",
  "errors": ["email is required", "password is required"]
}
```

- `401 Unauthorized`
```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

- `500 Internal Server Error`
```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

### Frontend <-> Backend Contract
- Login page sends `POST /api/auth/login`.
- On `200`: store auth state, redirect.
- On `400/401`: show form/auth errors.

### Database Contract
- Table: `users(id, name, email, password_hash)`
- Behavior: lookup by email + password verification.

---

## 2) Shopping Cart

### Endpoints
- `GET /api/cart`
- `POST /api/cart/items`
- `PUT /api/cart/items/{movieId}`
- `DELETE /api/cart/items/{movieId}`

### Auth
Required.

### 2.1 GET /api/cart
Response `200 OK`:
```json
{
  "success": true,
  "cart": {
    "items": [
      {
        "movieId": "tt0372784",
        "title": "Batman Begins",
        "unitPrice": 10.0,
        "quantity": 2,
        "subtotal": 20.0
      }
    ],
    "totalItems": 2,
    "totalPrice": 20.0
  }
}
```

### 2.2 POST /api/cart/items
Body:
```json
{
  "movieId": "tt0372784",
  "quantity": 1
}
```

Validation:
- `movieId` required
- `quantity` optional (default 1), must be integer >= 1

Response `200 OK` when added/updated.

### 2.3 PUT /api/cart/items/{movieId}
Body:
```json
{
  "quantity": 3
}
```

Validation:
- `quantity` required, integer >= 1

Response `200 OK` when updated.

### 2.4 DELETE /api/cart/items/{movieId}
Response `200 OK` when removed.

### Common Error Responses (all cart endpoints)
- `400 Bad Request` validation issues
- `401 Unauthorized` auth required
- `404 Not Found` movie/item not found
- `500 Internal Server Error`

### Frontend <-> Backend Contract
- Cart page loads with `GET /api/cart`.
- Add/update/remove call corresponding cart endpoint.

### Database Contract
- `cart_items(user_id, movie_id, quantity)`
- FKs to `users` and `movies`.

---

## 3) Movie Details

### Endpoint
`GET /api/movies/{movieId}`

### Auth
Not required.

### Response `200 OK`
```json
{
  "success": true,
  "movie": {
    "id": "tt0372784",
    "title": "Batman Begins",
    "year": 2005,
    "director": "Christopher Nolan",
    "rating": 8.2,
    "genres": [
      { "id": 1, "name": "Action" },
      { "id": 2, "name": "Drama" }
    ],
    "stars": [
      { "id": "nm0000288", "name": "Christian Bale" },
      { "id": "nm0000323", "name": "Michael Caine" }
    ],
    "price": 10.0
  }
}
```

### Errors
- `404 Not Found` movie not found
- `500 Internal Server Error`

### Frontend <-> Backend Contract
- Movie page requests by movie id.

### Database Contract
- Join tables: `movies`, `ratings`, `genres`, `genres_in_movies`, `stars`, `stars_in_movies`.

---

## 4) Star Details

### Endpoint
`GET /api/stars/{starId}`

### Auth
Not required.

### Response `200 OK`
```json
{
  "success": true,
  "star": {
    "id": "nm0000288",
    "name": "Christian Bale",
    "birthYear": 1974,
    "movies": [
      { "id": "tt0372784", "title": "Batman Begins", "year": 2005 },
      { "id": "tt0468569", "title": "The Dark Knight", "year": 2008 }
    ]
  }
}
```

### Errors
- `404 Not Found` star not found
- `500 Internal Server Error`

### Database Contract
- Join: `stars`, `stars_in_movies`, `movies`.

---

## 5) Search Movies

### Endpoint
`GET /api/movies/search`

### Auth
Not required.

### Query Parameters
- `keyword` (optional)
- `year` (optional)
- `genre` (optional)
- `actor` (optional)
- `page` (optional)
- `size` (optional)

### Response `200 OK`
```json
{
  "success": true,
  "results": [
    {
      "id": "tt12345",
      "title": "Batman Begins",
      "year": 2005,
      "rating": 8.2
    }
  ],
  "page": 1,
  "totalResults": 120
}
```

### Errors
- `400 Bad Request`
- `500 Internal Server Error`

---

## 6) Browse by Genre

### Endpoint
`GET /api/movies/genre/{genreName}`

Example:
`/api/movies/genre/Action?page=1&size=20`

### Auth
Not required.

### Response `200 OK`
```json
{
  "success": true,
  "genre": "Action",
  "movies": [
    {
      "id": "tt111",
      "title": "Mad Max",
      "year": 2015
    }
  ]
}
```

---

## 7) Browse by Title Letter

### Endpoint
`GET /api/movies/title/{letter}`

Example:
`/api/movies/title/B?page=1&size=20`

### Auth
Not required.

### Response `200 OK`
```json
{
  "success": true,
  "letter": "B",
  "movies": [
    {
      "id": "tt222",
      "title": "Batman",
      "year": 2008
    }
  ]
}
```

---

## 8) Movie List

### Endpoint
`GET /api/movies`

Example:
`/api/movies?page=1&size=20&sort=rating`

### Auth
Not required.

### Current Status
Implemented in Phase 2.

### Current Response (implemented)
Backend currently returns a plain JSON array:
```json
[
  {
    "id": "tt333",
    "title": "Inception",
    "year": 2010,
    "director": "Christopher Nolan"
  }
]
```

### Target Response (planned)
```json
{
  "success": true,
  "movies": [
    {
      "id": "tt333",
      "title": "Inception",
      "year": 2010,
      "rating": 8.8
    }
  ],
  "page": 1
}
```

---

## Interface Consistency Notes
- Frontend currently consumes `GET /api/movies` array response and renders movie cards/list.
- Backend currently maps `movies` table via JPA entity `Movie`.
- Future endpoints in this specification are the implementation contract for next phases.
