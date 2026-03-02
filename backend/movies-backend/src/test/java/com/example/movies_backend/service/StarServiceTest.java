package com.example.movies_backend.service;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

class StarServiceTest {

    /**
     * Phase 3 Current Behavior:
     * getStarById() is a placeholder and returns null.
     */
    @Test
    void getStarById_returnsNull_inPhase3() {
        StarService starService = new StarService();

        Map<String, Object> result = starService.getStarById("nm123");

        assertNull(
            result,
            "Phase 3: getStarById() is a placeholder and should return null"
        );
    }

    /**
     * Phase 4 Expected Behavior:
     * getStarById() should return star details and related movies.
     * Disabled until implementation is completed.
     */
    //@Disabled("Phase 4: getStarById() should return star details and related movies")
    @Test
    void getStarById_shouldReturnStarData_inPhase4() {
        StarService starService = new StarService();

        Map<String, Object> result = starService.getStarById("nm123");

        assertNotNull(
            result,
            "Phase 4 expected: getStarById() should return non-null star data"
        );

        assertTrue(
            result.containsKey("name"),
            "Phase 4 expected: star data should contain 'name' field"
        );

        assertTrue(
            result.containsKey("movies"),
            "Phase 4 expected: star data should contain related 'movies'"
        );
    }
}