package com.kits.apichatapp.config;

import com.kits.apichatapp.service.Authen.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String username;

        // check jwt token
        if (authHeader == null || !authHeader.startsWith("Bearer")) {
            filterChain.doFilter(request, response); // if token don't have, pass request and response to the next filter
            return;
        }
        // extract token from authHeader and username
        jwt = authHeader.substring(7); //begin Index at 7 because need to eliminate "Bearer"
        username = jwtService.extractUsername(jwt);// todo extract username from JWT token;
    }
}
