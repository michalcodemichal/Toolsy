package com.toolsy.service;

import com.toolsy.dto.request.CreateReviewRequest;
import com.toolsy.dto.response.ReviewResponse;
import com.toolsy.model.Review;
import com.toolsy.model.Tool;
import com.toolsy.model.User;
import com.toolsy.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ToolService toolService;
    private final UserService userService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, ToolService toolService, UserService userService) {
        this.reviewRepository = reviewRepository;
        this.toolService = toolService;
        this.userService = userService;
    }

    public ReviewResponse createReview(Long userId, Long toolId, CreateReviewRequest request) {
        if (reviewRepository.existsByToolIdAndUserId(toolId, userId)) {
            throw new RuntimeException("Użytkownik już dodał recenzję do tego narzędzia");
        }

        Tool tool = toolService.getToolEntity(toolId);
        User user = userService.findById(userId);

        Review review = new Review();
        review.setTool(tool);
        review.setUser(user);
        review.setRating(request.getRating());
        review.setComment(request.getComment() != null ? request.getComment().trim() : null);

        Review saved = reviewRepository.save(review);
        return mapToResponse(saved);
    }

    public List<ReviewResponse> getReviewsByTool(Long toolId) {
        return reviewRepository.findByToolIdOrderByCreatedAtDesc(toolId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ReviewResponse mapToResponse(Review review) {
        ReviewResponse response = new ReviewResponse();
        response.setId(review.getId());
        response.setToolId(review.getTool().getId());
        response.setUserId(review.getUser().getId());
        response.setUsername(review.getUser().getUsername());
        response.setFirstName(review.getUser().getFirstName());
        response.setLastName(review.getUser().getLastName());
        response.setRating(review.getRating());
        response.setComment(review.getComment());
        response.setCreatedAt(review.getCreatedAt());
        return response;
    }
}
