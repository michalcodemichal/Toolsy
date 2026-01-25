package com.toolsy.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "Odpowiedź zawierająca informacje o recenzji narzędzia")
public class ReviewResponse {
    @Schema(description = "ID recenzji", example = "1")
    private Long id;

    @Schema(description = "ID narzędzia", example = "10")
    private Long toolId;

    @Schema(description = "ID użytkownika", example = "5")
    private Long userId;

    @Schema(description = "Nazwa użytkownika", example = "jan.kowalski")
    private String username;

    @Schema(description = "Imię użytkownika", example = "Jan")
    private String firstName;

    @Schema(description = "Nazwisko użytkownika", example = "Kowalski")
    private String lastName;

    @Schema(description = "Ocena w skali 1-5", example = "5")
    private Integer rating;

    @Schema(description = "Treść recenzji", example = "Świetne narzędzie, polecam!")
    private String comment;

    @Schema(description = "Data dodania recenzji")
    private LocalDateTime createdAt;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
