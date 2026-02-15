package com.example.movies_backend.controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200")
public class StarController {

    /**
     * Logic:
     * Returns single star details by id.
     *
     * Params:
     * starId path variable.
     *
     * Return:
     * HTTP 501 Not Implemented for Phase 2 (contract only).
     */
    @GetMapping("/stars/{starId}")
    public ResponseEntity<?> getStarById(@PathVariable String starId) {
        return ResponseEntity.status(HttpStatus.NOT_IMPLEMENTED)
                .body(Map.of(
                        "page", "star-details",
                        "endpoint", "GET /api/stars/{starId}",
                        "starId", starId,
                        "message", "You are on Star Details page. Star details implementation is planned for next phase."));
    }
}
