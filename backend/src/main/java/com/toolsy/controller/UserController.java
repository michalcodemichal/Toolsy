package com.toolsy.controller;

import com.toolsy.dto.response.UserResponse;
import com.toolsy.security.UserPrincipal;
import com.toolsy.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/users")
@Tag(name = "Użytkownicy", description = "API do zarządzania użytkownikami")
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @Operation(summary = "Pobierz aktualnego użytkownika", description = "Zwraca informacje o zalogowanym użytkowniku", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Informacje o użytkowniku", content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    public ResponseEntity<UserResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        UserResponse user = userService.getUserById(userPrincipal.getId());
        return ResponseEntity.ok(user);
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Pobierz wszystkich użytkowników", description = "Zwraca listę wszystkich użytkowników (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista użytkowników", content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Pobierz użytkownika po ID", description = "Zwraca szczegóły użytkownika o podanym ID (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Szczegóły użytkownika", content = @Content(schema = @Schema(implementation = UserResponse.class))),
        @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<UserResponse> getUserById(@Parameter(description = "ID użytkownika", required = true) @PathVariable Long id) {
        UserResponse user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Dezaktywuj użytkownika", description = "Dezaktywuje konto użytkownika (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Użytkownik dezaktywowany"),
        @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<Void> deactivateUser(@Parameter(description = "ID użytkownika", required = true) @PathVariable Long id) {
        userService.deactivateUser(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Aktywuj użytkownika", description = "Aktywuje konto użytkownika (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Użytkownik aktywowany"),
        @ApiResponse(responseCode = "404", description = "Użytkownik nie znaleziony"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<Void> activateUser(@Parameter(description = "ID użytkownika", required = true) @PathVariable Long id) {
        userService.activateUser(id);
        return ResponseEntity.ok().build();
    }
}







