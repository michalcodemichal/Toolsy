package com.toolsy.security;

import com.toolsy.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, UserDetailsService userDetailsService) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String path = request.getRequestURI();
        String method = request.getMethod();
        
        if (isPublicPath(path, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");
        System.out.println("Request: " + method + " " + path);
        System.out.println("Authorization header: " + (authHeader != null ? "Present" : "Missing"));

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            try {
                String token = authHeader.substring(7);
                String username = jwtService.extractUsername(token);
                System.out.println("Extracted username: " + username);

                if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                    try {
                        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                        System.out.println("User authorities: " + userDetails.getAuthorities());

                        if (jwtService.validateToken(token, username)) {
                            UsernamePasswordAuthenticationToken authentication =
                                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                            authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                            SecurityContextHolder.getContext().setAuthentication(authentication);
                            System.out.println("Authentication set successfully");
                        } else {
                            System.out.println("Token validation failed");
                        }
                    } catch (Exception e) {
                        System.err.println("Błąd podczas weryfikacji tokenu: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                System.err.println("Błąd podczas przetwarzania tokenu: " + e.getMessage());
                e.printStackTrace();
            }
        } else {
            System.out.println("No Authorization header or not Bearer token");
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path, String method) {
        if (path.startsWith("/api/auth/") ||
            path.startsWith("/api/health/") ||
            path.startsWith("/h2-console/") ||
            path.startsWith("/swagger-ui/") ||
            path.startsWith("/v3/api-docs/") ||
            path.startsWith("/uploads/")) {
            return true;
        }
        
        if (path.startsWith("/api/tools/")) {
            return "GET".equalsIgnoreCase(method);
        }
        
        return false;
    }
}



