package com.distributedstorage.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import java.util.List;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // JwtFilter — intercepts every request and validates JWT tokens before reaching controllers
    private final JwtFilter jwtFilter;

    public SecurityConfig(JwtFilter jwtFilter) {
        this.jwtFilter = jwtFilter;
    }

    // Configures which routes are public and which require authentication
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        http
            // CORS — allow requests from React frontend running on localhost:5173
            // Without this, browser blocks cross-origin requests even through Vite proxy
            .cors(cors -> cors.configurationSource(request -> {
                CorsConfiguration config = new CorsConfiguration();

                // Allow only our frontend origin
                config.setAllowedOrigins(List.of("http://localhost:5173"));

                // Allow standard HTTP methods used by our REST API
                config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

                // Allow all headers — including Authorization header for JWT
                config.setAllowedHeaders(List.of("*"));

                // Allow credentials (cookies, Authorization headers) to be sent
                config.setAllowCredentials(true);

                return config;
            }))

            // Disable CSRF — not needed for stateless REST APIs using JWT
            .csrf(csrf -> csrf.disable())

            // Stateless sessions — JWT handles auth, no server-side session storage
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            // Define route authorization rules
            .authorizeHttpRequests(auth -> auth
                // Public routes — no token required
                .requestMatchers("/auth/**").permitAll()       // login endpoint
                .requestMatchers("/users/register").permitAll() // registration endpoint
                .requestMatchers("/").permitAll()               // root
                .requestMatchers("/swagger-ui/**").permitAll()  // API docs UI
                .requestMatchers("/v3/api-docs/**").permitAll() // OpenAPI spec
                // All other routes require a valid JWT token
                .anyRequest().authenticated()
            )

            // Add JWT filter before Spring's default username/password filter
            // This ensures token validation happens first on every request
            .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}