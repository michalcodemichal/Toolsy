package com.toolsy.dto.response;

import com.toolsy.model.RentalStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Schema(description = "Odpowiedź zawierająca informacje o wypożyczeniu")
public class RentalResponse {
    @Schema(description = "ID wypożyczenia", example = "1")
    private Long id;
    @Schema(description = "ID użytkownika", example = "1")
    private Long userId;
    @Schema(description = "Imię użytkownika", example = "Jan")
    private String userFirstName;
    @Schema(description = "Nazwisko użytkownika", example = "Kowalski")
    private String userLastName;
    @Schema(description = "ID narzędzia", example = "1")
    private Long toolId;
    @Schema(description = "Nazwa narzędzia", example = "Wiertarka udarowa")
    private String toolName;
    @Schema(description = "Data rozpoczęcia wypożyczenia", example = "2024-01-15")
    private LocalDate startDate;
    @Schema(description = "Data zakończenia wypożyczenia", example = "2024-01-20")
    private LocalDate endDate;
    @Schema(description = "Ilość wypożyczonych sztuk", example = "1")
    private Integer quantity;
    @Schema(description = "Całkowita cena wypożyczenia", example = "127.50")
    private BigDecimal totalPrice;
    @Schema(description = "Status wypożyczenia", example = "PENDING")
    private RentalStatus status;
    @Schema(description = "Dodatkowe uwagi", example = "Proszę o dostarczenie do domu")
    private String notes;
    @Schema(description = "Data utworzenia")
    private LocalDateTime createdAt;
    @Schema(description = "Data ostatniej aktualizacji")
    private LocalDateTime updatedAt;
    @Schema(description = "Data zwrotu narzędzia")
    private LocalDateTime returnedAt;

    public RentalResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
    }

    public String getToolName() {
        return toolName;
    }

    public void setToolName(String toolName) {
        this.toolName = toolName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public RentalStatus getStatus() {
        return status;
    }

    public void setStatus(RentalStatus status) {
        this.status = status;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDateTime getReturnedAt() {
        return returnedAt;
    }

    public void setReturnedAt(LocalDateTime returnedAt) {
        this.returnedAt = returnedAt;
    }
}



