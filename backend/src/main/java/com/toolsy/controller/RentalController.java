package com.toolsy.controller;

import com.toolsy.dto.request.CreateRentalRequest;
import com.toolsy.dto.response.RentalResponse;
import com.toolsy.security.UserPrincipal;
import com.toolsy.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/rentals")
@Tag(name = "Wypożyczenia", description = "API do zarządzania wypożyczeniami narzędzi")
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
    @Operation(summary = "Utwórz wypożyczenie", description = "Tworzy nowe wypożyczenie narzędzia", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Wypożyczenie utworzone pomyślnie", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe lub narzędzie niedostępne"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "404", description = "Narzędzie nie znalezione")
    })
    public ResponseEntity<RentalResponse> createRental(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateRentalRequest request) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        System.out.println("Tworzenie wypożyczenia dla użytkownika: " + userPrincipal.getId());
        System.out.println("Request: " + request.getToolId() + ", " + request.getStartDate() + " - " + request.getEndDate());
        RentalResponse rental = rentalService.createRental(userPrincipal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(rental);
    }

    @GetMapping("/my")
    @Operation(summary = "Pobierz moje wypożyczenia", description = "Zwraca listę wypożyczeń zalogowanego użytkownika", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista wypożyczeń użytkownika", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    public ResponseEntity<List<RentalResponse>> getMyRentals(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<RentalResponse> rentals = rentalService.getUserRentals(userPrincipal.getId());
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Pobierz szczegóły wypożyczenia", description = "Zwraca szczegóły wypożyczenia o podanym ID", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Szczegóły wypożyczenia", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Wypożyczenie nie znalezione"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    public ResponseEntity<RentalResponse> getRentalById(@Parameter(description = "ID wypożyczenia", required = true) @PathVariable Long id) {
        RentalResponse rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Zatwierdź wypożyczenie", description = "Zatwierdza wypożyczenie (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wypożyczenie zatwierdzone", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Wypożyczenie nie znalezione"),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowy status wypożyczenia"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<RentalResponse> approveRental(@Parameter(description = "ID wypożyczenia", required = true) @PathVariable Long id) {
        RentalResponse rental = rentalService.approveRental(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Zakończ wypożyczenie", description = "Oznacza wypożyczenie jako zakończone (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wypożyczenie zakończone", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Wypożyczenie nie znalezione"),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowy status wypożyczenia"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<RentalResponse> completeRental(@Parameter(description = "ID wypożyczenia", required = true) @PathVariable Long id) {
        RentalResponse rental = rentalService.completeRental(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}/cancel")
    @Operation(summary = "Anuluj wypożyczenie", description = "Anuluje wypożyczenie", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Wypożyczenie anulowane", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "404", description = "Wypożyczenie nie znalezione"),
        @ApiResponse(responseCode = "400", description = "Nieprawidłowy status wypożyczenia"),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    public ResponseEntity<RentalResponse> cancelRental(@Parameter(description = "ID wypożyczenia", required = true) @PathVariable Long id) {
        RentalResponse rental = rentalService.cancelRental(id);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Pobierz wszystkie wypożyczenia", description = "Zwraca listę wszystkich wypożyczeń (wymaga uprawnień ADMIN)", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista wszystkich wypożyczeń", content = @Content(schema = @Schema(implementation = RentalResponse.class))),
        @ApiResponse(responseCode = "401", description = "Brak autoryzacji"),
        @ApiResponse(responseCode = "403", description = "Brak uprawnień")
    })
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        List<RentalResponse> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }
}



