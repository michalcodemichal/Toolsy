package com.toolsy.controller;

import com.toolsy.dto.request.LoginRequest;
import com.toolsy.dto.request.RegisterRequest;
import com.toolsy.dto.response.JwtResponse;
import com.toolsy.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
@Tag(name = "Autoryzacja", description = "API do logowania i rejestracji użytkowników")
public class AuthController {
    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    @Operation(summary = "Logowanie użytkownika", description = "Uwierzytelnia użytkownika i zwraca token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Logowanie pomyślne", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "401", description = "Nieprawidłowe dane logowania"),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe")
    })
    public ResponseEntity<JwtResponse> login(@Valid @RequestBody LoginRequest request) {
        JwtResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/register")
    @Operation(summary = "Rejestracja nowego użytkownika", description = "Tworzy nowe konto użytkownika i zwraca token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Rejestracja pomyślna", content = @Content(schema = @Schema(implementation = JwtResponse.class))),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe lub użytkownik już istnieje"),
        @ApiResponse(responseCode = "409", description = "Użytkownik o podanym username lub email już istnieje")
    })
    public ResponseEntity<JwtResponse> register(@Valid @RequestBody RegisterRequest request) {
        JwtResponse response = authService.register(request);
        return ResponseEntity.ok(response);
    }
}







