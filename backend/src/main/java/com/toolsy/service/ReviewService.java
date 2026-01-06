package com.toolsy.service;

import com.toolsy.dto.request.CreateReviewRequest;
import com.toolsy.dto.response.ReviewResponse;
import com.toolsy.model.Review;
import com.toolsy.model.Tool;
import com.toolsy.model.User;
import com.toolsy.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ToolService toolService;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, @Lazy ToolService toolService, @Lazy UserService userService) {
        this.reviewRepository = reviewRepository;
        this.toolService = toolService;
        this.userService = userService;
    }

    public ReviewResponse createReview(Long userId, CreateReviewRequest request) {
        User user = userService.findById(userId);
        Tool tool = toolService.getToolEntity(request.getToolId());

        if (reviewRepository.existsByUserIdAndToolId(user.getId(), tool.getId())) {
            throw new RuntimeException("Użytkownik już dodał recenzję dla tego narzędzia.");
        }

        Review review = new Review();
        review.setUser(user);
        review.setTool(tool);
        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        toolService.updateToolRating(tool.getId()); // Update tool's average rating
        return mapToResponse(savedReview);
    }

    public ReviewResponse updateReview(Long reviewId, Long userId, CreateReviewRequest request) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Recenzja nie znaleziona"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Nie masz uprawnień do edycji tej recenzji");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        toolService.updateToolRating(review.getTool().getId()); // Update tool's average rating
        return mapToResponse(updatedReview);
    }

    public void deleteReview(Long reviewId, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Recenzja nie znaleziona"));

        if (!review.getUser().getId().equals(userId)) {
            throw new RuntimeException("Nie masz uprawnień do usunięcia tej recenzji");
        }

        Long toolId = review.getTool().getId();
        reviewRepository.delete(review);
        toolService.updateToolRating(toolId); // Update tool's average rating
    }

    public List<ReviewResponse> getReviewsByToolId(Long toolId) {
        return reviewRepository.findByToolId(toolId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Recenzja nie znaleziona"));
        return mapToResponse(review);
    }

    public BigDecimal getAverageRating(Long toolId) {
        Double avg = reviewRepository.findAverageRatingByToolId(toolId);
        if (avg == null) {
            return BigDecimal.ZERO;
        }
        return BigDecimal.valueOf(avg).setScale(2, RoundingMode.HALF_UP);
    }

    public Long getReviewCount(Long toolId) {
        return reviewRepository.countByToolId(toolId);
    }

    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setUserId(review.getUser().getId());
        response.setUsername(review.getUser().getUsername());
        response.setToolId(review.getTool().getId());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}

