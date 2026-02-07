# API Specification

Movie Rental Application REST contract shared between frontend and backend.

## Base URL

`http://localhost:8081/api`

---

## 1. Login

**Purpose**  
Allow a user to log in with email + password and establish an authenticated session.

**Endpoint**  
`POST /api/auth/login`

**Authentication**  
Not required.

**Request**

- Headers: `Content-Type: application/json`
- Body:

```json
{
  "email": "user@example.com",
  "password": "P@ssw0rd!"
}
```

**Validation**

- `email` is required and must be a valid email.
- `password` is required and must be non-empty.

**Responses**

- `200 OK` - success

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

- `400 Bad Request` - validation error

```json
{
  "success": false,
  "message": "Validation error",
  "errors": ["email is required", "password is required"]
}
```

- `401 Unauthorized` - invalid credentials

```json
{
  "success": false,
  "message": "Invalid email or password"
}
```

- `500 Internal Server Error` - unexpected failure

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

**Frontend &lt;-&gt; Backend**

- Page: Login page.
- On submit: send `POST /api/auth/login` with JSON body.
- On `200`: mark user as authenticated and redirect to browse/search.
- On `400`: show validation errors near inputs.
- On `401`: show "Invalid email or password".

**Database Contract**

- Table: `users`
- Columns: `id` (PK), `name`, `email` (unique), `password_hash`
- Behavior: fetch by `email`, verify password, create session on success; otherwise return `401`.

---

## 2. Shopping Cart

**Purpose**  
Allow an authenticated user to view and manage cart items before checkout.

**Endpoints**

- `GET /api/cart` - fetch current cart
- `POST /api/cart/items` - add an item (or increase quantity)
- `PUT /api/cart/items/{movieId}` - update quantity
- `DELETE /api/cart/items/{movieId}` - remove item

**Authentication**  
Required (session cookie, e.g., `JSESSIONID`).

**Shared Request Rules**

- Headers: `Content-Type: application/json` for `POST` and `PUT`.
- Validation:
  - User must be authenticated; else `401`.
  - `movieId` must exist; else `404`.
  - `quantity` must be integer &gt;= 1 for add/update; else `400`.

### 2.1 GET /api/cart

**Request Parameters**: none

**Responses**

- `200 OK`

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

- `401 Unauthorized`

```json
{
  "success": false,
  "message": "Authentication required"
}
```

- `500 Internal Server Error`

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

### 2.2 POST /api/cart/items

**Body**

```json
{
  "movieId": "tt0372784",
  "quantity": 1
}
```

**Validation**

- `movieId` required, non-empty.
- `quantity` optional; default 1; if provided, must be integer &gt;= 1.

**Responses**

- `200 OK` - item added or quantity increased

```json
{
  "success": true,
  "message": "Item added to cart",
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

- `400 Bad Request` - validation error

```json
{
  "success": false,
  "message": "Validation error",
  "errors": ["movieId is required", "quantity must be &gt;= 1"]
}
```

- `401 Unauthorized`

```json
{
  "success": false,
  "message": "Authentication required"
}
```

- `404 Not Found` - movie missing

```json
{
  "success": false,
  "message": "Movie not found"
}
```

- `500 Internal Server Error`

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

### 2.3 PUT /api/cart/items/{movieId}

**Body**

```json
{
  "quantity": 3
}
```

**Validation**

- `quantity` required, integer &gt;= 1.

**Responses**

- `200 OK` - quantity updated

```json
{
  "success": true,
  "message": "Cart item updated",
  "cart": {
    "items": [
      {
        "movieId": "tt0372784",
        "title": "Batman Begins",
        "unitPrice": 10.0,
        "quantity": 3,
        "subtotal": 30.0
      }
    ],
    "totalItems": 3,
    "totalPrice": 30.0
  }
}
```

- `400 Bad Request` - validation error

```json
{
  "success": false,
  "message": "Validation error",
  "errors": ["quantity is required", "quantity must be &gt;= 1"]
}
```

- `401 Unauthorized`

```json
{
  "success": false,
  "message": "Authentication required"
}
```

- `404 Not Found` - item not in cart or movie missing

```json
{
  "success": false,
  "message": "Cart item not found"
}
```

- `500 Internal Server Error`

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

### 2.4 DELETE /api/cart/items/{movieId}

**Request Parameters**: none

**Responses**

- `200 OK` - item removed

```json
{
  "success": true,
  "message": "Item removed from cart",
  "cart": {
    "items": [],
    "totalItems": 0,
    "totalPrice": 0.0
  }
}
```

- `401 Unauthorized`

```json
{
  "success": false,
  "message": "Authentication required"
}
```

- `404 Not Found` - item not in cart

```json
{
  "success": false,
  "message": "Cart item not found"
}
```

- `500 Internal Server Error`

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

**Frontend &lt;-&gt; Backend**

- Page: Shopping Cart.
- On load: `GET /api/cart`.
- Add to cart: `POST /api/cart/items` with `{ movieId, quantity }`.
- Update quantity: `PUT /api/cart/items/{movieId}` with `{ quantity }`.
- Remove item: `DELETE /api/cart/items/{movieId}`.
- If unauthenticated, any cart request returns `401`; frontend should redirect to Login or show "Please login to access your cart".

**Database Contract**

- Tables: `cart_items` (links `users` and `movies`)
- Columns: `user_id` (FK -&gt; users.id), `movie_id` (FK -&gt; movies.id), `quantity` (int &gt;= 1)
- Behavior:
  - Add: if `(user_id, movie_id)` exists, increment `quantity`; else insert.
  - Update: set `quantity` for `(user_id, movie_id)`.
  - Remove: delete `(user_id, movie_id)`.
  - `GET /api/cart`: return items with movie details and totals.

---

## 3. Movie Details

**Purpose**  
Display full details for a movie, including genres, rating, and stars.

**Endpoint**  
`GET /api/movies/{movieId}`

**Authentication**  
Not required.

**Request**

- Headers: `Content-Type: application/json`
- Path variable: `{movieId}` (required)

**Validation**

- `movieId` required and non-empty.
- If `movieId` not found, return `404 Not Found`.

**Responses**

- `200 OK`

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

- `404 Not Found`

```json
{
  "success": false,
  "message": "Movie not found"
}
```

- `500 Internal Server Error`

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

**Frontend &lt;-&gt; Backend**

- Page: Single Movie page.
- When a movie is clicked: navigate to `/movies/{movieId}` and call `GET /api/movies/{movieId}`.
- On `200`: render movie info, genres, and stars (stars link to `/stars/{starId}`).
- On `404`: show "Movie not found" with navigation back.

**Database Contract**

- Tables: `movies`, `ratings`, `genres`, `stars`, `genres_in_movies`, `stars_in_movies`
- Columns:
  - `movies`: `id`, `title`, `year`, `director`, `price`
  - `ratings`: `movie_id`, `rating`
  - `genres`: `id`, `name`
  - `stars`: `id`, `name`, `birthYear`
  - `genres_in_movies`: `genre_id`, `movie_id`
  - `stars_in_movies`: `star_id`, `movie_id`
- Behavior: join related tables to return movie details, rating, genres, and stars.

---

## 4. Star Details

**Purpose**  
Display details for a star and the movies they appear in.

**Endpoint**  
`GET /api/stars/{starId}`

**Authentication**  
Not required.

**Request**

- Headers: `Content-Type: application/json`
- Path variable: `{starId}` (required)

**Validation**

- `starId` required and non-empty.
- If `starId` not found, return `404 Not Found`.

**Responses**

- `200 OK`

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

- `404 Not Found`

```json
{
  "success": false,
  "message": "Star not found"
}
```

- `500 Internal Server Error`

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

**Frontend &lt;-&gt; Backend**

- Page: Single Star page.
- When a star name is clicked from movie details: navigate to `/stars/{starId}` and call `GET /api/stars/{starId}`.
- On `200`: render star info and movie list (links back to movie details).
- On `404`: show "Star not found".

**Database Contract**

- Tables: `stars`, `movies`, `stars_in_movies`
- Columns:
  - `stars`: `id`, `name`, `birthYear`
  - `movies`: `id`, `title`, `year`
  - `stars_in_movies`: `star_id`, `movie_id`
- Behavior: fetch star, join to movies via `stars_in_movies`, return combined result.

---

## 5. Searching Feature

**Purpose**  
Allow users to search for movies using keywords and optional filters such as title, year, genre, or actor name.

**Endpoint**  
`GET /api/movies/search`

**Authentication**  
Not required.

**Request**

- Headers: `Content-Type: application/json`
- Query Parameters (example): `keyword=batman&year=2020&genre=Action&actor=Tom&page=1&size=20`
- Parameters:
  - `keyword`: optional search text.
  - `year`: optional movie year.
  - `genre`: optional genre filter.
  - `actor`: optional actor name.
  - `page`: pagination page number.
  - `size`: number of results per page.

**Responses**

- `200 OK` - Success

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

- `400 Bad Request`

```json
{
  "success": false,
  "message": "Invalid query parameters"
}
```

- `500 Internal Server Error`

```json
{
  "success": false,
  "message": "Unexpected server error"
}
```

**Frontend &lt;-&gt; Backend**

- User enters search filters.
- Frontend calls `/api/movies/search`.
- Backend returns matching movies.
- Frontend displays results.

**Database Contract**

- Backend searches `movies` table joined with `genres` and `actors` tables.

---

## 6. Browse by Movie Genre Feature

**Purpose**  
Allow users to browse movies filtered by genre.

**Endpoint**  
`GET /api/movies/genre/{genreName}`

Example: `/api/movies/genre/Action?page=1&size=20`

**Authentication**  
Not required.

**Request**

- Path variable: `genreName` (genre name).
- Query Parameters:
  - `page`: pagination page.
  - `size`: results per page.

**Responses**

- `200 OK`

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

**Frontend &lt;-&gt; Backend**

- User selects a genre → frontend requests movies → movies displayed.

**Database Contract**

- Movies are fetched using movie–genre relationship table.

---

## 7. Browse by Movie Title Feature

**Purpose**  
Allow users to browse movies alphabetically by title.

**Endpoint**  
`GET /api/movies/title/{letter}`

Example: `/api/movies/title/B?page=1&size=20`

**Authentication**  
Not required.

**Request**

- Path variable: `letter` (starting letter).
- Query Parameters:
  - `page`, `size`: pagination parameters.

**Responses**

- `200 OK`

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

**Frontend &lt;-&gt; Backend**

- User clicks a letter → movies starting with that letter appear.

**Database Contract**

- Backend filters movies where title starts with provided letter.

---

## 8. Movie List Feature

**Purpose**  
Display a paginated list of movies on the main browsing page.

**Endpoint**  
`GET /api/movies`

Example: `/api/movies?page=1&size=20&sort=rating`

**Authentication**  
Not required.

**Request**

- Query Parameters:
  - `page`: page number.
  - `size`: number of movies per page.
  - `sort`: sorting field (rating, year, title).

**Responses**

- `200 OK`

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

**Frontend &lt;-&gt; Backend**

- Homepage loads → frontend calls movies endpoint → movies displayed.

**Database Contract**

- Movies retrieved from `movies` table with sorting and pagination.