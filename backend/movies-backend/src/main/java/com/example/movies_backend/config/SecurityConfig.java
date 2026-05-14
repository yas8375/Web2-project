package com.example.movies_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.example.movies_backend.security.JwtAuthenticationFilter;
import com.example.movies_backend.service.JwtService;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtService jwtService) throws Exception {
        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtService);

        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(Customizer.withDefaults())
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .logout(AbstractHttpConfigurer::disable)
                .sessionManagement(session ->
                        // Session is used for cart/checkout flows in this project.
                        // JWT is also supported, but we still allow server sessions when needed.
                        session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .exceptionHandling(exception -> exception.authenticationEntryPoint(
                        (request, response, authException) ->
                                response.sendError(HttpServletResponse.SC_UNAUTHORIZED)))
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/login", "/api/signup", "/api/register").permitAll()
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/movies",
                                "/api/movies/**",
                                "/api/genres",
                                "/api/titles",
                                "/api/stars/**")
                        .permitAll()
                        .requestMatchers(
                                "/api/cart",
                                "/api/cart/**",
                                "/api/checkout",
                                "/api/checkout/**",
                                "/api/orders",
                                "/api/orders/**",
                                "/api/rentals",
                                "/api/rentals/**",
                                "/api/profile",
                                "/api/profile/**",
                                "/api/customer",
                                "/api/customer/**",
                                "/api/customers",
                                "/api/customers/**")
                        .authenticated()
                        .anyRequest().permitAll())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
