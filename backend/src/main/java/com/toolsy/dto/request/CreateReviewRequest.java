package com.toolsy.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class CreateReviewRequest {
    @NotNull(message = "ID narzędzia jest wymagane")
    private Long toolId;

    @NotNull(message = "Ocena jest wymagana")
    @Min(value = 1, message = "Ocena musi być między 1 a 5")
    @Max(value = 5, message = "Ocena musi być między 1 a 5")
    private Integer rating;

    @Size(max = 1000, message = "Komentarz nie może przekraczać 1000 znaków")
    private String comment;

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}

