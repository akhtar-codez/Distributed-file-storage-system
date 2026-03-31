package com.distributedstorage.backend.config;

import com.distributedstorage.backend.util.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtFilter extends OncePerRequestFilter {

    // JwtUtil to extract and validate token
    private final JwtUtil jwtUtil;

    // UserDetailsService to load user from database using email from token
    private final UserDetailsService userDetailsService;

    // Constructor injection
    public JwtFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    // This method runs on every incoming HTTP request
    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // Step 1 — Extract Authorization header from request
        String authHeader = request.getHeader("Authorization");

        // Step 2 — Check if header exists and starts with "Bearer "
        // If not, skip authentication and continue to next filter
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Step 3 — Extract the token by removing "Bearer " prefix
        String token = authHeader.substring(7);

        // Step 4 — Extract email from token — wrapped in try/catch for expired/invalid tokens
        String email;
        try {
            email = jwtUtil.extractEmail(token);
        } catch (Exception e) {
            // Token is expired or invalid — skip authentication, continue as unauthenticated
            filterChain.doFilter(request, response);
            return;
        }

        // Step 5 — If email exists and no authentication is set yet in context
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Step 6 — Load user details from database using email
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // Step 7 — Validate token against user details
            if (jwtUtil.isTokenValid(token)) {

                // Step 8 — Create authentication token and set in security context
                // This tells Spring Security the request is authenticated
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );

                // Step 9 — Set authentication in security context
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Step 10 — Continue to next filter in chain
        filterChain.doFilter(request, response);
    }
}