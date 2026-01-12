package com.toolsy.dto.response;

import com.toolsy.model.ToolStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Schema(description = "Odpowiedź zawierająca informacje o narzędziu")
public class ToolResponse {
    @Schema(description = "ID narzędzia", example = "1")
    private Long id;
    @Schema(description = "Nazwa narzędzia", example = "Wiertarka udarowa")
    private String name;
    @Schema(description = "Opis narzędzia", example = "Profesjonalna wiertarka udarowa z funkcją młotkowania")
    private String description;
    @Schema(description = "Kategoria narzędzia", example = "Elektryczne")
    private String category;
    @Schema(description = "Cena dzienna wypożyczenia", example = "25.50")
    private BigDecimal dailyPrice;
    @Schema(description = "Ilość dostępnych sztuk", example = "5")
    private Integer quantity;
    @Schema(description = "URL zdjęcia narzędzia", example = "/uploads/tool.jpg")
    private String imageUrl;
    @Schema(description = "Status narzędzia", example = "AVAILABLE")
    private ToolStatus status;
    @Schema(description = "Data utworzenia")
    private LocalDateTime createdAt;
    @Schema(description = "Data ostatniej aktualizacji")
    private LocalDateTime updatedAt;

    public ToolResponse() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public BigDecimal getDailyPrice() {
        return dailyPrice;
    }

    public void setDailyPrice(BigDecimal dailyPrice) {
        this.dailyPrice = dailyPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public ToolStatus getStatus() {
        return status;
    }

    public void setStatus(ToolStatus status) {
        this.status = status;
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
}







