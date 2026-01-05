package com.toolsy.controller;

import com.toolsy.dto.request.CreateReviewRequest;
import com.toolsy.dto.response.ReviewResponse;
import com.toolsy.security.UserPrincipal;
import com.toolsy.service.ReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {
    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateReviewRequest request) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ReviewResponse review = reviewService.createReview(userPrincipal.getId(), request);
        return ResponseEntity.status(HttpStatus.CREATED).body(review);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @Valid @RequestBody CreateReviewRequest request) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ReviewResponse review = reviewService.updateReview(id, userPrincipal.getId(), request);
        return ResponseEntity.ok(review);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        reviewService.deleteReview(id, userPrincipal.getId());
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tool/{toolId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByTool(@PathVariable Long toolId) {
        List<ReviewResponse> reviews = reviewService.getReviewsByToolId(toolId);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/tool/{toolId}/my")
    public ResponseEntity<ReviewResponse> getMyReviewForTool(
            @PathVariable Long toolId,
            @AuthenticationPrincipal UserPrincipal userPrincipal) {
        if (userPrincipal == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        ReviewResponse review = reviewService.getReviewByUserAndTool(userPrincipal.getId(), toolId);
        return ResponseEntity.ok(review);
    }
}

