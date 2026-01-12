package com.toolsy.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;

@Schema(description = "Żądanie utworzenia wypożyczenia")
public class CreateRentalRequest {
    @NotNull
    @Schema(description = "ID narzędzia do wypożyczenia", example = "1", required = true)
    private Long toolId;

    @NotNull
    @Schema(description = "Data rozpoczęcia wypożyczenia (format: YYYY-MM-DD)", example = "2024-01-15", required = true)
    private LocalDate startDate;

    @NotNull
    @Schema(description = "Data zakończenia wypożyczenia (format: YYYY-MM-DD)", example = "2024-01-20", required = true)
    private LocalDate endDate;

    @NotNull
    @Schema(description = "Ilość wypożyczanych sztuk", example = "1", required = true)
    private Integer quantity = 1;

    @Schema(description = "Dodatkowe uwagi do wypożyczenia", example = "Proszę o dostarczenie do domu")
    private String notes;

    public CreateRentalRequest() {
    }

    public Long getToolId() {
        return toolId;
    }

    public void setToolId(Long toolId) {
        this.toolId = toolId;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}



