package com.example.movies_backend.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.cache.CacheManager;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.example.movies_backend.dto.MovieSummaryDTO;
import com.example.movies_backend.dto.SingleStarDTO;
import com.example.movies_backend.service.StarService;

@WebMvcTest(StarController.class)
class StarControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private StarService starService;

  @MockitoBean
  private CacheManager cacheManager;

  @Test
  void getStarById_shouldReturnStarDetails() throws Exception {
    when(starService.getStarById("nm0000138", 1, 20)).thenReturn(
        new SingleStarDTO(
            "nm0000138",
            "Star Name",
            1970,
            List.of(new MovieSummaryDTO("tt1", "Movie One", 2000, "Director One", 8.1f))));

    mockMvc.perform(get("/api/stars/nm0000138"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value("nm0000138"))
        .andExpect(jsonPath("$.name").value("Star Name"))
        .andExpect(jsonPath("$.movies[0].id").value("tt1"));
  }

  @Test
  void getStarById_shouldReturn404_whenStarMissing() throws Exception {
    when(starService.getStarById("nm-missing", 1, 20)).thenReturn(null);

    mockMvc.perform(get("/api/stars/nm-missing"))
        .andExpect(status().isNotFound())
        .andExpect(jsonPath("$.message").value("Star not found"))
        .andExpect(jsonPath("$.starId").value("nm-missing"));
  }
}
