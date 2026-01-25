package com.toolsy.controller;

import com.toolsy.dto.request.CreateReviewRequest;
import com.toolsy.dto.response.ReviewResponse;
import com.toolsy.security.UserPrincipal;
import com.toolsy.service.ReviewService;
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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/tools/{toolId}/reviews")
@Tag(name = "Recenzje", description = "API do zarządzania recenzjami narzędzi")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping
    @Operation(summary = "Pobierz recenzje narzędzia", description = "Zwraca listę recenzji dla wybranego narzędzia")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Lista recenzji", content = @Content(schema = @Schema(implementation = ReviewResponse.class)))
    })
    public ResponseEntity<List<ReviewResponse>> getReviews(
            @Parameter(description = "ID narzędzia", required = true) @PathVariable Long toolId) {
        return ResponseEntity.ok(reviewService.getReviewsByTool(toolId));
    }

    @PostMapping
    @Operation(summary = "Dodaj recenzję narzędzia", description = "Dodaje recenzję dla wybranego narzędzia", security = @SecurityRequirement(name = "bearerAuth"))
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Recenzja dodana", content = @Content(schema = @Schema(implementation = ReviewResponse.class))),
            @ApiResponse(responseCode = "400", description = "Nieprawidłowe dane wejściowe"),
            @ApiResponse(responseCode = "401", description = "Brak autoryzacji")
    })
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Parameter(description = "ID narzędzia", required = true) @PathVariable Long toolId,
            @Valid @RequestBody CreateReviewRequest request) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ReviewResponse response = reviewService.createReview(userPrincipal.getId(), toolId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
