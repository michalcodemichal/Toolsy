package com.toolsy.controller;

import com.toolsy.dto.request.CreateRentalRequest;
import com.toolsy.dto.response.RentalResponse;
import com.toolsy.security.UserPrincipal;
import com.toolsy.service.RentalService;
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
public class RentalController {
    private final RentalService rentalService;

    @Autowired
    public RentalController(RentalService rentalService) {
        this.rentalService = rentalService;
    }

    @PostMapping
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
    public ResponseEntity<List<RentalResponse>> getMyRentals(
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        List<RentalResponse> rentals = rentalService.getUserRentals(userPrincipal.getId());
        return ResponseEntity.ok(rentals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RentalResponse> getRentalById(@PathVariable Long id) {
        RentalResponse rental = rentalService.getRentalById(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RentalResponse> approveRental(@PathVariable Long id) {
        RentalResponse rental = rentalService.approveRental(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}/complete")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RentalResponse> completeRental(@PathVariable Long id) {
        RentalResponse rental = rentalService.completeRental(id);
        return ResponseEntity.ok(rental);
    }

    @PutMapping("/{id}/cancel")
    public ResponseEntity<RentalResponse> cancelRental(@PathVariable Long id) {
        RentalResponse rental = rentalService.cancelRental(id);
        return ResponseEntity.ok(rental);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RentalResponse>> getAllRentals() {
        List<RentalResponse> rentals = rentalService.getAllRentals();
        return ResponseEntity.ok(rentals);
    }
}



