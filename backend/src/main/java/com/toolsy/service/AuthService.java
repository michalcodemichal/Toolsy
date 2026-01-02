package com.toolsy.service;

import com.toolsy.dto.request.LoginRequest;
import com.toolsy.dto.request.RegisterRequest;
import com.toolsy.dto.response.JwtResponse;
import com.toolsy.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserService userService;

    @Autowired
    public AuthService(AuthenticationManager authenticationManager, JwtService jwtService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    public JwtResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        User user = userService.findByUsername(request.getUsername());

        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        List<String> roles = Collections.singletonList(user.getRole().name());

        return new JwtResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles
        );
    }

    public JwtResponse register(RegisterRequest request) {
        User user = userService.createUser(request);
        String hashedPassword = user.getPassword();
        
        String token = jwtService.generateToken(user.getUsername(), user.getRole().name());
        List<String> roles = Collections.singletonList(user.getRole().name());

        JwtResponse response = new JwtResponse(
                token,
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                roles
        );
        response.setHashedPassword(hashedPassword);
        return response;
    }
}

