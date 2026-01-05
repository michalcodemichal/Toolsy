package com.toolsy.controller;

import com.toolsy.dto.response.JwtResponse;
import com.toolsy.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth/oauth2")
public class OAuth2Controller {
    private final AuthService authService;

    @Autowired
    public OAuth2Controller(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/success")
    public void oauth2Success(
            @AuthenticationPrincipal OAuth2User oauth2User,
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        try {
            System.out.println("=== OAuth2Controller /success endpoint called ===");
            System.out.println("OAuth2User: " + (oauth2User != null ? "present" : "null"));
            
            if (oauth2User == null) {
                System.err.println("OAuth2User is null - trying to get from SecurityContext");
                org.springframework.security.core.Authentication auth = 
                    org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
                System.out.println("SecurityContext Authentication: " + (auth != null ? auth.getName() : "null"));
                if (auth != null && auth.getPrincipal() != null) {
                    System.out.println("Principal class: " + auth.getPrincipal().getClass().getName());
                    if (auth.getPrincipal() instanceof OAuth2User) {
                        oauth2User = (OAuth2User) auth.getPrincipal();
                        System.out.println("Found OAuth2User in SecurityContext");
                    }
                }
            }
            
            if (oauth2User == null) {
                System.err.println("OAuth2User is still null after checking SecurityContext");
                response.sendRedirect("http://localhost:3000/login?error=oauth_user_null");
                return;
            }

            Map<String, Object> attributes = oauth2User.getAttributes();
            String email = (String) attributes.get("email");
            String name = (String) attributes.get("name");
            String googleId = (String) attributes.get("sub");

            System.out.println("OAuth2 login - Email: " + email + ", Name: " + name + ", GoogleId: " + googleId);

            if (email == null || googleId == null) {
                System.err.println("Missing required OAuth2 attributes");
                response.sendRedirect("http://localhost:3000/login?error=oauth_missing_attributes");
                return;
            }

            JwtResponse jwtResponse = authService.loginOrRegisterWithGoogle(
                    email,
                    name,
                    googleId
            );

            String redirectUrl = String.format(
                    "http://localhost:3000/auth/callback?token=%s&userId=%s&username=%s&email=%s&firstName=%s&lastName=%s",
                    java.net.URLEncoder.encode(jwtResponse.getToken(), "UTF-8"),
                    jwtResponse.getId(),
                    java.net.URLEncoder.encode(jwtResponse.getUsername(), "UTF-8"),
                    java.net.URLEncoder.encode(jwtResponse.getEmail(), "UTF-8"),
                    java.net.URLEncoder.encode(jwtResponse.getFirstName() != null ? jwtResponse.getFirstName() : "", "UTF-8"),
                    java.net.URLEncoder.encode(jwtResponse.getLastName() != null ? jwtResponse.getLastName() : "", "UTF-8")
            );

            System.out.println("Redirecting to: " + redirectUrl);
            response.sendRedirect(redirectUrl);
        } catch (Exception e) {
            System.err.println("Error in OAuth2 success handler: " + e.getMessage());
            e.printStackTrace();
            String errorMsg = e.getMessage() != null ? e.getMessage() : "Unknown error";
            System.err.println("Full error: " + errorMsg);
            for (StackTraceElement element : e.getStackTrace()) {
                System.err.println(element.toString());
            }
            response.sendRedirect("http://localhost:3000/login?error=oauth_error&details=" + 
                java.net.URLEncoder.encode(errorMsg, "UTF-8"));
        }
    }

    @GetMapping("/failure")
    public void oauth2Failure(HttpServletResponse response) throws IOException {
        response.sendRedirect("http://localhost:3000/login?error=oauth_failed");
    }
}

