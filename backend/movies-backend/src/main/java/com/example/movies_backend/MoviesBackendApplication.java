package com.example.movies_backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class MoviesBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesBackendApplication.class, args);
	}

}
