package com.example.movies_backend.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.movies_backend.dto.SingleStarDTO;
import com.example.movies_backend.service.StarService;

@RestController
@RequestMapping("/api")
@CrossOrigin(originPatterns = {"http://localhost:*", "http://127.0.0.1:*"})
public class StarController {

    @Autowired
    private StarService starService;

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
        SingleStarDTO star = starService.getStarById(starId);
        if (star == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "message", "Star not found",
                            "starId", starId));
        }
        return ResponseEntity.ok(star);
    }
}
