CREATE TABLE movies (
    id VARCHAR(10) PRIMARY KEY,
    title VARCHAR(100) NOT NULL,
    year INTEGER NOT NULL,
    director VARCHAR(100) NOT NULL
);

CREATE TABLE stars (
    id VARCHAR(10) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    birthYear INTEGER 
);

CREATE TABLE stars_in_movies (
    starId VARCHAR(10) NOT NULL REFERENCES stars(id),
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id)
);

CREATE TABLE genres (
    id SERIAL PRIMARY KEY, 
    name VARCHAR(32) NOT NULL
);

CREATE TABLE genres_in_movies (
    genreId INTEGER NOT NULL REFERENCES genres(id),
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id)
);

CREATE TABLE creditcards (
    id VARCHAR(20) PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    expiration DATE NOT NULL
);

CREATE TABLE customers (
    id SERIAL PRIMARY KEY,
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    ccId VARCHAR(20) NOT NULL REFERENCES creditcards(id),
    address VARCHAR(200) NOT NULL,
    email VARCHAR(50) NOT NULL,
    password VARCHAR(20) NOT NULL
);

CREATE TABLE sales (
    id SERIAL PRIMARY KEY,
    customerId INTEGER NOT NULL REFERENCES customers(id),
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id),
    saleDate DATE NOT NULL
);

CREATE TABLE ratings (
    movieId VARCHAR(10) NOT NULL REFERENCES movies(id),
    rating FLOAT NOT NULL,
    numVotes INTEGER NOT NULL
);